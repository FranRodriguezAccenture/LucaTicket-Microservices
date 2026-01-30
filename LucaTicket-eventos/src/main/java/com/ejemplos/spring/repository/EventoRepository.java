package com.ejemplos.spring.repository;

import com.ejemplos.spring.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}
