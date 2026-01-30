package com.ejemplos.spring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "La fecha del evento es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_evento", nullable = false)
    private LocalDate fechaEvento;

    @NotNull(message = "La hora del evento es obligatoria")
    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "hora_evento", nullable = false)
    private LocalTime horaEvento;

    @NotNull(message = "El precio mínimo es obligatorio")
    @PositiveOrZero(message = "El precio mínimo debe ser 0 o mayor")
    @Column(name = "precio_min", nullable = false)
    private Double precioMin;

    @NotNull(message = "El precio máximo es obligatorio")
    @Positive(message = "El precio máximo debe ser mayor que 0")
    @Column(name = "precio_max", nullable = false)
    private Double precioMax;

    @NotBlank(message = "La localidad es obligatoria")
    @Column(nullable = false)
    private String localidad;

    @NotBlank(message = "El género es obligatorio")
    @Column(nullable = false)
    private String genero;

    @NotBlank(message = "El recinto es obligatorio")
    @Column(nullable = false)
    private String recinto;

    // Getters y Setters
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
