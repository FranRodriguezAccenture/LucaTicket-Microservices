package com.ejemplos.spring.service.impl;

import com.ejemplos.spring.model.entity.Compra;
import com.ejemplos.spring.service.*;
import com.ejemplos.spring.model.dto.*;
import com.ejemplos.spring.repository.CompraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VentaServiceImpl implements VentaService {
    
    private static final Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);
    
    @Autowired
    private CompraRepository compraRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${lucabanking.url:http://localhost:8082}")
    private String lucaBankingUrl;
    
    private final Random random = new Random();
    
    @Override
    public CompraResponseDTO procesarCompra(CompraRequestDTO compraRequest) {
        log.info("🛒 INICIANDO PROCESO DE COMPRA");
        log.info("📧 Email: {}", compraRequest.getEmail());
        log.info("🎫 Evento ID: {}", compraRequest.getEventoId());
        
        try {
            // 1. VALIDAR DATOS BÁSICOS
            validarDatosCompra(compraRequest);
            log.info("✅ Validación básica completada");
            
            // 2. SIMULAR OBTENER INFO DEL EVENTO (debería venir del microservicio de eventos)
            // Por ahora usamos valores fijos para prueba
            double precioMin = 25.0;
            double precioMax = 100.0;
            String nombreEvento = "Evento " + compraRequest.getEventoId();
            log.info("💰 Precios del evento: Min={}€, Max={}€", precioMin, precioMax);
            
            // 3. GENERAR PRECIO ALEATORIO ENTRE MIN Y MAX
            double precioPagado = generarPrecioAleatorio(precioMin, precioMax);
            log.info("🎲 Precio generado aleatoriamente: {}€", precioPagado);
            
            // 4. PREPARAR REQUEST PARA PASAELA (LucaBanking)
            PasarelaRequestDTO pasarelaRequest = crearRequestPasarela(compraRequest, precioPagado);
            log.info("💳 Datos tarjeta preparados para pasarela");
            
            // 5. LLAMAR AL MOCK DE PASAELA (tu MockPasarelaController)
            PasarelaResponseDTO pasarelaResponse = llamarPasarela(pasarelaRequest);
            log.info("🔄 Respuesta pasarela recibida - Código: {}", pasarelaResponse.getCodigo());
            
            // 6. DETERMINAR SI EL PAGO FUE EXITOSO
            boolean pagoExitoso = "200.0001".equals(pasarelaResponse.getCodigo());
            log.info("📊 Estado pago: {}", pagoExitoso ? "EXITOSO" : "FALLIDO");
            
            // 7. FILTRAR MENSAJE OFENSIVO (según requerimiento del PDF)
            String mensajeFiltrado = filtrarMensajeOfensivo(pasarelaResponse.getDescripcion());
            log.info("✂️ Mensaje filtrado (sin ofensas): {}", mensajeFiltrado);
            
            // 8. GUARDAR COMPRA EN BD
            Compra compra = guardarCompraEnBD(compraRequest, precioPagado, nombreEvento, 
                                            pasarelaResponse, mensajeFiltrado, pagoExitoso);
            log.info("💾 Compra guardada en BD con ID: {}", compra.getId());
            
            // 9. SIMULAR ENVÍO DE EMAIL
            simularEnvioEmail(compra, pagoExitoso);
            
            // 10. CREAR RESPUESTA PARA EL CLIENTE
            CompraResponseDTO respuesta = crearRespuestaCliente(compra, pagoExitoso, mensajeFiltrado);
            log.info("📤 Respuesta al cliente preparada - Estado: {}", respuesta.getStatus());
            
            return respuesta;
            
        } catch (IllegalArgumentException e) {
            log.error("❌ Error de validación: {}", e.getMessage());
            return crearRespuestaError("VALIDACIÓN FALLIDA: " + e.getMessage(), "ERROR_VALIDACION");
            
        } catch (Exception e) {
            log.error("💥 Error inesperado: {}", e.getMessage(), e);
            return crearRespuestaError("ERROR INTERNO: " + e.getMessage(), "ERROR_INTERNO");
        }
    }
    
    private void validarDatosCompra(CompraRequestDTO compraRequest) {
        // SOLO validaciones básicas
        if (compraRequest.getEmail() == null || compraRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (!compraRequest.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email no válido");
        }
        if (compraRequest.getEventoId() == null || compraRequest.getEventoId() <= 0) {
            throw new IllegalArgumentException("ID de evento no válido");
        }
        if (compraRequest.getDatosTarjeta() == null) {
            throw new IllegalArgumentException("Datos de tarjeta son obligatorios");
        }
        
        // QUITAR validaciones de tarjeta (las hará LucaBanking)
        // DatosTarjetaDTO tarjeta = compraRequest.getDatosTarjeta();
        // NO validar: formato tarjeta, CVV, fechas, etc.
    }
    
    private double generarPrecioAleatorio(double min, double max) {
        // Redondear a 2 decimales
        double precio = min + (max - min) * random.nextDouble();
        return Math.round(precio * 100.0) / 100.0;
    }
    
    private PasarelaRequestDTO crearRequestPasarela(CompraRequestDTO compraRequest, double precioPagado) {
        PasarelaRequestDTO request = new PasarelaRequestDTO();
        DatosTarjetaDTO tarjeta = compraRequest.getDatosTarjeta();
        
        request.setNombreTitular(tarjeta.getNombreTitular());
        request.setNumeroTarjeta(tarjeta.getNumeroTarjeta());
        request.setMesCaducidad(tarjeta.getMesCaducidad());
        request.setYearCaducidad(tarjeta.getYearCaducidad());
        request.setCvv(tarjeta.getCvv());
        request.setEmisor("LucaTicket");
        request.setConcepto("Entrada para evento ID: " + compraRequest.getEventoId());
        request.setCantidad(String.format("%.2f", precioPagado));
        
        return request;
    }
    
    private PasarelaResponseDTO llamarPasarela(PasarelaRequestDTO request) {
        String url = lucaBankingUrl + "/pasarela/compra";
        log.info("🌐 Llamando a pasarela: {}", url);
        
        try {
            // Llama a TU MockPasarelaController
            return restTemplate.postForObject(url, request, PasarelaResponseDTO.class);
            
        } catch (Exception e) {
            log.error("⚠️ Error llamando a pasarela: {}", e.getMessage());
            
            // Crear respuesta de fallback si el mock no responde
            PasarelaResponseDTO fallback = new PasarelaResponseDTO();
            fallback.setCodigo("500.0001");
            fallback.setDescripcion("El sistema se encuentra inestable. Inténtalo de nuevo, lechón.");
            return fallback;
        }
    }
    
    private String filtrarMensajeOfensivo(String mensajeOriginal) {
        if (mensajeOriginal == null) return "Sin mensaje disponible";
        
        // Filtrar palabras ofensivas según el PDF
        return mensajeOriginal
            .replace("lechón", "cliente")
            .replace("vago", "usuario")
            .replace("mendigo", "persona")
            .replace("majete", "estimado")
            .replace("animal", "usuario")
            .replace("imbécil", "cliente")
            .replace("idiota", "cliente");
    }
    
    private Compra guardarCompraEnBD(CompraRequestDTO compraRequest, double precioPagado, 
                                    String nombreEvento, PasarelaResponseDTO pasarelaResponse,
                                    String mensajeFiltrado, boolean pagoExitoso) {
        
        Compra compra = new Compra();
        compra.setEmail(compraRequest.getEmail());
        compra.setEventoId(compraRequest.getEventoId());
        compra.setNombreEvento(nombreEvento);
        compra.setPrecioPagado(precioPagado);
        
        // Generar código de transacción único
        String codigoTransaccion = "TXN" + System.currentTimeMillis() + random.nextInt(1000);
        compra.setCodigoTransaccion(codigoTransaccion);
        
        compra.setCodigoRespuestaPasarela(pasarelaResponse.getCodigo());
        compra.setMensaje(mensajeFiltrado);
        compra.setEstadoCompra(pagoExitoso ? "EXITOSA" : "FALLIDA");
        
        return compraRepository.save(compra);
    }
    
    private void simularEnvioEmail(Compra compra, boolean exito) {
        if (exito) {
            log.info("📧 [SIMULACIÓN] Email enviado a {} - Compra exitosa #{}", 
                    compra.getEmail(), compra.getCodigoTransaccion());
        } else {
            log.info("📧 [SIMULACIÓN] Email enviado a {} - Compra fallida", compra.getEmail());
        }
    }
    
    private CompraResponseDTO crearRespuestaCliente(Compra compra, boolean pagoExitoso, String mensaje) {
        CompraResponseDTO response = new CompraResponseDTO();
        
        response.setStatus(pagoExitoso ? "COMPLETADA" : "RECHAZADA");
        response.setCodigoTransaccion(compra.getCodigoTransaccion());
        response.setMensaje(mensaje);
        response.setPrecioPagado(compra.getPrecioPagado());
        response.setFechaCompra(compra.getFechaCompra());
        response.setEventoId(compra.getEventoId());
        response.setNombreEvento(compra.getNombreEvento());
        
        // AÑADIR ESTAS DOS LÍNEAS:
        response.setEmail(compra.getEmail());  // ← FALTABA
        response.setCodigoRespuesta(compra.getCodigoRespuestaPasarela());  // ← FALTABA
        
        // Personalizar mensaje según éxito
        if (pagoExitoso) {
            response.setMensaje("✅ " + mensaje + 
                              "\nCódigo de entrada: " + compra.getCodigoTransaccion().substring(0, 8));
        } else {
            response.setMensaje("❌ " + mensaje + 
                              "\nPor favor, verifica tus datos e intenta nuevamente.");
        }
        
        return response;
    }
    
    private CompraResponseDTO crearRespuestaError(String mensajeError, String tipoError) {
        CompraResponseDTO response = new CompraResponseDTO();
        response.setStatus("ERROR");
        response.setMensaje("🚫 " + mensajeError);
        response.setCodigoTransaccion("ERR-" + tipoError + "-" + System.currentTimeMillis());
        response.setFechaCompra(LocalDateTime.now());
        // Opcional: puedes setear email si lo tienes disponible
        return response;
    }
}