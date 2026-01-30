package com.ejemplos.spring.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaEvento;
    private LocalTime horaEvento;
    private Double precioMin;
    private Double precioMax;
    private String localidad;
    private String genero;
    private String recinto;
    
    // Getters y Setters (genera con Alt+Shift+S, R)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public LocalDate getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(LocalDate fechaEvento) { this.fechaEvento = fechaEvento; }
    
    public LocalTime getHoraEvento() { return horaEvento; }
    public void setHoraEvento(LocalTime horaEvento) { this.horaEvento = horaEvento; }
    
    public Double getPrecioMin() { return precioMin; }
    public void setPrecioMin(Double precioMin) { this.precioMin = precioMin; }
    
    public Double getPrecioMax() { return precioMax; }
    public void setPrecioMax(Double precioMax) { this.precioMax = precioMax; }
    
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    public String getRecinto() { return recinto; }
    public void setRecinto(String recinto) { this.recinto = recinto; }
}