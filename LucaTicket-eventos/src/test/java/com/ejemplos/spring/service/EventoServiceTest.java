package com.ejemplos.spring.service;

import com.ejemplos.spring.model.Evento;
import com.ejemplos.spring.repository.EventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventoServiceTest {

    @Mock
    private EventoRepository repository;

    @InjectMocks
    private EventoService service;

    private Evento evento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        evento = new Evento();
        evento.setId(1L);
        evento.setNombre("Festival Rock");
        evento.setDescripcion("Festival de música rock");
        evento.setFechaEvento(LocalDate.of(2026,1,30));
        evento.setHoraEvento(LocalTime.of(18,0));
        evento.setPrecioMin(50.0);
        evento.setPrecioMax(120.0);
        evento.setLocalidad("Madrid");
        evento.setGenero("Rock");
        evento.setRecinto("Estadio Central");
    }

    @Test
    void testCrearEvento() {
        when(repository.save(any(Evento.class))).thenReturn(evento);

        Evento creado = service.crear(evento);

        assertNotNull(creado);
        assertEquals("Festival Rock", creado.getNombre());
    }

    @Test
    void testObtenerPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(evento));

        Evento encontrado = service.obtenerPorId(1L);

        assertNotNull(encontrado);
        assertEquals("Festival Rock", encontrado.getNombre());
    }

    @Test
    void testBorrarEvento() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.borrar(1L));
        verify(repository, times(1)).deleteById(1L);
    }
}
