package com.ejemplos.spring.model.dto;

import jakarta.validation.constraints.*;

public class CompraRequestDTO {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;
    
    @NotNull(message = "El ID del evento es obligatorio")
    @Positive(message = "El ID del evento debe ser positivo")
    private Long eventoId;
    
    @NotNull(message = "Los datos de la tarjeta son obligatorios")
    private DatosTarjetaDTO datosTarjeta;
    
    // Constructores
    public CompraRequestDTO() {}
    
    public CompraRequestDTO(String email, Long eventoId, DatosTarjetaDTO datosTarjeta) {
        this.email = email;
        this.eventoId = eventoId;
        this.datosTarjeta = datosTarjeta;
    }
    
    // Getters y Setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Long getEventoId() {
        return eventoId;
    }
    
    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
    
    public DatosTarjetaDTO getDatosTarjeta() {
        return datosTarjeta;
    }
    
    public void setDatosTarjeta(DatosTarjetaDTO datosTarjeta) {
        this.datosTarjeta = datosTarjeta;
    }
}