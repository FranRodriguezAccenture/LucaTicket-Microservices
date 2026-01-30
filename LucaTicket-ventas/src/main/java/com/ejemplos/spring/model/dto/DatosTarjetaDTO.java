package com.ejemplos.spring.model.dto;

import jakarta.validation.constraints.*;

public class DatosTarjetaDTO {
    
    @NotBlank(message = "El nombre del titular es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,100}$", 
             message = "El nombre debe contener solo letras y espacios (3-100 caracteres)")
    private String nombreTitular;
    
    @NotBlank(message = "El número de tarjeta es obligatorio")
    @Pattern(regexp = "^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}$", 
             message = "Formato de tarjeta inválido. Use: XXXX-XXXX-XXXX-XXXX")
    private String numeroTarjeta;
    
    @NotBlank(message = "El mes de caducidad es obligatorio")
    @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Mes inválido (01-12)")
    private String mesCaducidad;
    
    @NotBlank(message = "El año de caducidad es obligatorio")
    @Pattern(regexp = "^20[2-9][0-9]$", message = "Año inválido (debe ser 2024 o posterior)")
    private String yearCaducidad;
    
    @NotBlank(message = "El CVV es obligatorio")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV inválido (3 o 4 dígitos)")
    private String cvv;
    
    @NotBlank(message = "El emisor es obligatorio")
    private String emisor;
    
    @NotBlank(message = "El concepto es obligatorio")
    private String concepto;
    
    // Constructores
    public DatosTarjetaDTO() {}
    
    public DatosTarjetaDTO(String nombreTitular, String numeroTarjeta, String mesCaducidad,
                          String yearCaducidad, String cvv, String emisor, String concepto) {
        this.nombreTitular = nombreTitular;
        this.numeroTarjeta = numeroTarjeta;
        this.mesCaducidad = mesCaducidad;
        this.yearCaducidad = yearCaducidad;
        this.cvv = cvv;
        this.emisor = emisor;
        this.concepto = concepto;
    }
    
    // Getters y Setters
    public String getNombreTitular() {
        return nombreTitular;
    }
    
    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }
    
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }
    
    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }
    
    public String getMesCaducidad() {
        return mesCaducidad;
    }
    
    public void setMesCaducidad(String mesCaducidad) {
        this.mesCaducidad = mesCaducidad;
    }
    
    public String getYearCaducidad() {
        return yearCaducidad;
    }
    
    public void setYearCaducidad(String yearCaducidad) {
        this.yearCaducidad = yearCaducidad;
    }
    
    public String getCvv() {
        return cvv;
    }
    
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    
    public String getEmisor() {
        return emisor;
    }
    
    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }
    
    public String getConcepto() {
        return concepto;
    }
    
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }
}