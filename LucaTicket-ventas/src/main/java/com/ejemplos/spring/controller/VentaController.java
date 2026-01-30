package com.ejemplos.spring.controller;

import com.ejemplos.spring.model.dto.CompraRequestDTO;
import com.ejemplos.spring.model.dto.CompraResponseDTO;
import com.ejemplos.spring.model.dto.PasarelaRequestDTO;
import com.ejemplos.spring.model.dto.PasarelaResponseDTO;
import com.ejemplos.spring.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "API para gestión de ventas de entradas")
public class VentaController {
    
    private static final Logger log = LoggerFactory.getLogger(VentaController.class);
    
    private final VentaService ventaService;
    
    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }
    
    @PostMapping("/compra")
    @Operation(summary = "Procesar compra de entradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Compra procesada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CompraResponseDTO> comprarEntradas(
            @Parameter(description = "Datos de la compra", required = true)
            @Valid @RequestBody CompraRequestDTO compraRequest) {
        
        log.info("📥 Recibida solicitud de compra - Email: {}, Evento ID: {}", 
                compraRequest.getEmail(), compraRequest.getEventoId());
        
        CompraResponseDTO respuesta = ventaService.procesarCompra(compraRequest);
        
        log.info("📤 Respuesta de compra generada - Status: {}, Transacción: {}", 
                respuesta.getStatus(), respuesta.getCodigoTransaccion());
        
        return ResponseEntity.ok(respuesta);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("✅ LucaTicket-ventas service is UP and RUNNING");
    }
    
    
}