package com.ejemplos.spring.service;

import com.ejemplos.spring.model.dto.CompraRequestDTO;
import com.ejemplos.spring.model.dto.CompraResponseDTO;

public interface VentaService {
    CompraResponseDTO procesarCompra(CompraRequestDTO compraRequest);
}