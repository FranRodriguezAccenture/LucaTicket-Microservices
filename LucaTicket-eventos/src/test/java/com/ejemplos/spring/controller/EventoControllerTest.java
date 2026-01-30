package com.ejemplos.spring.controller;

import com.ejemplos.spring.model.Evento;
import com.ejemplos.spring.service.EventoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(EventoController.class)
class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    //@MockBean
    private EventoService service;

    private Evento evento;

    @BeforeEach
    void setUp() {
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
    void testListarEventos() throws Exception {
        Mockito.when(service.listar()).thenReturn(Collections.singletonList(evento));

        mockMvc.perform(get("/eventos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Festival Rock"))
                .andExpect(jsonPath("$[0].localidad").value("Madrid"));
    }

    @Test
    void testCrearEvento() throws Exception {
        Mockito.when(service.crear(Mockito.any(Evento.class))).thenReturn(evento);

        mockMvc.perform(post("/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Festival Rock"));
    }

    @Test
    void testActualizarEvento() throws Exception {
        Evento actualizado = new Evento();
        actualizado.setNombre("Festival Pop");
        Mockito.when(service.actualizar(Mockito.eq(1L), Mockito.any(Evento.class))).thenReturn(actualizado);

        mockMvc.perform(put("/eventos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Festival Pop"));
    }
}
