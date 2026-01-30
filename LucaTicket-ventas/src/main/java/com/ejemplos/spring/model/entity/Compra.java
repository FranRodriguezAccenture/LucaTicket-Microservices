package com.ejemplos.spring.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
public class Compra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String email;
    
    @Column(name = "evento_id", nullable = false)
    private Long eventoId;
    
    @Column(name = "nombre_evento")
    private String nombreEvento;
    
    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;
    
    @Column(name = "precio_pagado", nullable = false)
    private Double precioPagado;
    
    @Column(name = "codigo_transaccion", unique = true)
    private String codigoTransaccion;
    
    @Column(name = "estado_compra")
    private String estadoCompra; // "EXITOSA", "FALLIDA", "PENDIENTE"
    
    @Column(name = "codigo_respuesta_pasarela")
    private String codigoRespuestaPasarela; // Ej: "200.0001", "400.0001"
    
    @Column(columnDefinition = "TEXT")
    private String mensaje; // Mensaje de la pasarela (filtrado)
    
    // Constructores
    public Compra() {
        this.fechaCompra = LocalDateTime.now();
        this.estadoCompra = "PENDIENTE";
    }
    
    public Compra(String email, Long eventoId, String nombreEvento, 
                  Double precioPagado, String codigoTransaccion, 
                  String codigoRespuestaPasarela, String mensaje, String estadoCompra) {
        this.email = email;
        this.eventoId = eventoId;
        this.nombreEvento = nombreEvento;
        this.fechaCompra = LocalDateTime.now();
        this.precioPagado = precioPagado;
        this.codigoTransaccion = codigoTransaccion;
        this.codigoRespuestaPasarela = codigoRespuestaPasarela;
        this.mensaje = mensaje;
        this.estadoCompra = estadoCompra != null ? estadoCompra : "PENDIENTE";
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getNombreEvento() {
        return nombreEvento;
    }
    
    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }
    
    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }
    
    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
    
    public Double getPrecioPagado() {
        return precioPagado;
    }
    
    public void setPrecioPagado(Double precioPagado) {
        this.precioPagado = precioPagado;
    }
    
    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }
    
    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }
    
    public String getEstadoCompra() {
        return estadoCompra;
    }
    
    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
    }
    
    public String getCodigoRespuestaPasarela() {
        return codigoRespuestaPasarela;
    }
    
    public void setCodigoRespuestaPasarela(String codigoRespuestaPasarela) {
        this.codigoRespuestaPasarela = codigoRespuestaPasarela;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    // Método PrePersist para asegurar fecha de creación
    @PrePersist
    protected void onCreate() {
        if (fechaCompra == null) {
            fechaCompra = LocalDateTime.now();
        }
        if (estadoCompra == null) {
            estadoCompra = "PENDIENTE";
        }
    }
    
    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", eventoId=" + eventoId +
                ", nombreEvento='" + nombreEvento + '\'' +
                ", fechaCompra=" + fechaCompra +
                ", precioPagado=" + precioPagado +
                ", codigoTransaccion='" + codigoTransaccion + '\'' +
                ", estadoCompra='" + estadoCompra + '\'' +
                '}';
    }
}