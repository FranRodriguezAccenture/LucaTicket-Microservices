package com.ejemplos.spring.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ejemplos.spring.model.dto.PasarelaRequestDTO;
import com.ejemplos.spring.model.dto.PasarelaResponseDTO;

@FeignClient(name = "lucabanking", url = "${lucabanking.url}")
public interface PasarelaPagoClient {
    
    @PostMapping("${lucabanking.endpoint.compra}")
    PasarelaResponseDTO procesarPago(@RequestBody PasarelaRequestDTO request);
}