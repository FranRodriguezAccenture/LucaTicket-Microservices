package com.ejemplos.spring.model.dto;

public class PasarelaResponseDTO {
    private String codigo;
    private String descripcion;
    private String nombreTitular;
    private String numeroTarjeta;
    private String mesCaducidad;
    private String yearCaducidad;
    private String cvv;
    private String emisor;
    private String concepto;
    private String cantidad;
    
    // Getters y Setters (genera con Alt+Shift+S, R)
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }
    
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }
    
    public String getMesCaducidad() { return mesCaducidad; }
    public void setMesCaducidad(String mesCaducidad) { this.mesCaducidad = mesCaducidad; }
    
    public String getYearCaducidad() { return yearCaducidad; }
    public void setYearCaducidad(String yearCaducidad) { this.yearCaducidad = yearCaducidad; }
    
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
    
    public String getEmisor() { return emisor; }
    public void setEmisor(String emisor) { this.emisor = emisor; }
    
    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
    
    public String getCantidad() { return cantidad; }
    public void setCantidad(String cantidad) { this.cantidad = cantidad; }
}