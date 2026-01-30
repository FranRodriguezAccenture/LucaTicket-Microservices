package com.ejemplos.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient  // Para Eureka
@EnableFeignClients     // Para Feign Clients
public class LucaTicketVentasApplication {
    public static void main(String[] args) {
        SpringApplication.run(LucaTicketVentasApplication.class, args);
        System.out.println("✅ LucaTicket-ventas service iniciado en puerto 8082");
    }
}