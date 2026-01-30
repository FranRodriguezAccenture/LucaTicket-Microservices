package com.ejemplos.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @GetMapping("/redis")
    public ResponseEntity<Map<String, String>> checkRedisHealth() {
        try {
            var connection = redisConnectionFactory.getConnection();
            boolean isConnected = connection.ping().equals("PONG");
            connection.close();
            
            if (isConnected) {
                return ResponseEntity.ok(Map.of(
                    "status", "UP",
                    "message", "Redis está conectado"
                ));
            } else {
                return ResponseEntity.status(503).body(Map.of(
                    "status", "DOWN",
                    "message", "Redis no responde"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(503).body(Map.of(
                "status", "DOWN",
                "message", "Error conectando a Redis: " + e.getMessage()
            ));
        }
    }
}