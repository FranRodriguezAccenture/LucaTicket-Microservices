package com.ejemplos.spring.service;

import com.ejemplos.spring.model.Evento;
import com.ejemplos.spring.repository.EventoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository repository;

    public EventoService(EventoRepository repository) {
        this.repository = repository;
    }

    public Evento crear(Evento evento) {
        return repository.save(evento);
    }

    public Evento obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Evento no encontrado con ID " + id));
    }

    public List<Evento> listar() {
        return repository.findAll();
    }

    public Evento actualizar(Long id, Evento evento) {
        Evento existente = obtenerPorId(id);

        // Actualización segura
        if (evento.getNombre() != null) existente.setNombre(evento.getNombre());
        if (evento.getDescripcion() != null) existente.setDescripcion(evento.getDescripcion());
        if (evento.getFechaEvento() != null) existente.setFechaEvento(evento.getFechaEvento());
        if (evento.getHoraEvento() != null) existente.setHoraEvento(evento.getHoraEvento());
        if (evento.getPrecioMin() != null) existente.setPrecioMin(evento.getPrecioMin());
        if (evento.getPrecioMax() != null) existente.setPrecioMax(evento.getPrecioMax());
        if (evento.getLocalidad() != null) existente.setLocalidad(evento.getLocalidad());
        if (evento.getGenero() != null) existente.setGenero(evento.getGenero());
        if (evento.getRecinto() != null) existente.setRecinto(evento.getRecinto());

        return repository.save(existente);
    }

    public void borrar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Evento no encontrado para borrar con ID " + id);
        }
    }
}
