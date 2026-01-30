package com.ejemplos.spring.controller;

import com.ejemplos.spring.model.Evento;
import com.ejemplos.spring.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @PostMapping
    public Evento crear(@Valid @RequestBody Evento evento) {
        Evento creado = service.crear(evento);
        System.out.println("Evento creado: " + creado.getNombre() + " (ID: " + creado.getId() + ")");
        return creado;
    }

    @GetMapping("/{id}")
    public Evento obtener(@PathVariable Long id) {
        Evento evento = service.obtenerPorId(id);
        if (evento != null) {
            System.out.println("Detalle del evento obtenido: " + evento.getNombre() + " (ID: " + evento.getId() + ")");
        } else {
            System.out.println("Intento de obtener evento inexistente (ID: " + id + ")");
        }
        return evento;
    }

    @GetMapping
    public List<Evento> listar() {
        List<Evento> eventos = service.listar();
        System.out.println("Listado de eventos: " + eventos.size() + " eventos encontrados");
        return eventos;
    }

    @PutMapping("/{id}")
    public Evento actualizar(@PathVariable Long id, @RequestBody Evento evento) {
        Evento actualizado = service.actualizar(id, evento);
        if (actualizado != null) {
            System.out.println("Evento actualizado: " + actualizado.getNombre() + " (ID: " + actualizado.getId() + ")");
        } else {
            System.out.println("Intento de actualizar evento inexistente (ID: " + id + ")");
        }
        return actualizado;
    }

    @DeleteMapping("/{id}")
    public void borrar(@PathVariable Long id) {
        service.borrar(id);
        System.out.println("Evento eliminado (ID: " + id + ")");
    }
}
