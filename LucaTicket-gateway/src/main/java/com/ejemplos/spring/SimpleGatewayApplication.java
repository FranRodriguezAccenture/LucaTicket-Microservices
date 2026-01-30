package com.ejemplos.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SimpleGatewayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SimpleGatewayApplication.class, args);
        System.out.println("========================================");
        System.out.println("✅ SIMPLE GATEWAY INICIADO");
        System.out.println("📍 Puerto: 8765");
        System.out.println("🛣️  Rutas disponibles:");
        System.out.println("   • GET  /api/eventos");
        System.out.println("   • GET  /api/eventos/{id}");
        System.out.println("   • POST /api/ventas/compra");
        System.out.println("   • GET  /api/ventas/health");
        System.out.println("========================================");
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}