package com.ejemplos.spring.model.dto;

import java.time.LocalDateTime;

public class CompraResponseDTO {
    private String status;
    private String mensaje;
    private String codigoTransaccion;
    private LocalDateTime fechaCompra;
    private Double precioPagado;
    private String email;
    private Long eventoId;
    private String codigoRespuesta;
    private String nombreEvento;
    
    // Constructores
    public CompraResponseDTO() {}
    
    public CompraResponseDTO(String status, String mensaje, String codigoTransaccion, 
                            LocalDateTime fechaCompra, Double precioPagado, String email, 
                            Long eventoId, String codigoRespuesta) {
        this.status = status;
        this.mensaje = mensaje;
        this.codigoTransaccion = codigoTransaccion;
        this.fechaCompra = fechaCompra;
        this.precioPagado = precioPagado;
        this.email = email;
        this.eventoId = eventoId;
        this.codigoRespuesta = codigoRespuesta;
        this.nombreEvento = nombreEvento;
    }
    
    // Getters y Setters (genera rápidamente en Eclipse: Alt+Shift+S, R)
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public String getCodigoTransaccion() { return codigoTransaccion; }
    public void setCodigoTransaccion(String codigoTransaccion) { this.codigoTransaccion = codigoTransaccion; }
    
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }
    
    public Double getPrecioPagado() { return precioPagado; }
    public void setPrecioPagado(Double precioPagado) { this.precioPagado = precioPagado; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    
    public String getCodigoRespuesta() { return codigoRespuesta; }
    public void setCodigoRespuesta(String codigoRespuesta) { this.codigoRespuesta = codigoRespuesta; }
    
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; } 
}