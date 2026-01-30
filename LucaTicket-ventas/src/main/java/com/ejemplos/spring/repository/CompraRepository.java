package com.ejemplos.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ejemplos.spring.model.entity.Compra;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    List<Compra> findByEmail(String email);
    
    List<Compra> findByEventoId(Long eventoId);
    
    Optional<Compra> findByCodigoTransaccion(String codigoTransaccion);
    
    long countByEstadoCompra(String estadoCompra);
}