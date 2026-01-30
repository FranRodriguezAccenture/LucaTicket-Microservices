package com.ejemplos.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
        System.out.println("=========================================");
        System.out.println("✅ CONFIG SERVER INICIADO");
        System.out.println("📍 Puerto: 8888");
        System.out.println("🔗 GitHub: https://github.com/FranRodriguezAccenture/LucaTicket-Microservices");
        System.out.println("📁 Carpeta: /config");
        System.out.println("=========================================");
    }
}
