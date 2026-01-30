package com.ejemplos.spring.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ejemplos.spring.model.dto.EventoDTO;

@FeignClient(name = "lucaticket-eventos", url = "${eventos.service.url}")
public interface EventoServiceClient {
    
    @GetMapping("/eventos/{id}")
    EventoDTO getEventoById(@PathVariable Long id);
}
