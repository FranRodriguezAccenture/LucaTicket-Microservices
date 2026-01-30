package com.ejemplos.spring.controller;


import com.ejemplos.spring.model.CacheStats;
import com.ejemplos.spring.service.CacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cache")
@Tag(name = "Cache Management", description = "Gestión de caché distribuida")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @PostMapping("/clear/all")
    @Operation(summary = "Limpiar todas las cachés")
    public ResponseEntity<Map<String, String>> clearAllCaches() {
        cacheService.clearAllCaches();
        return ResponseEntity.ok(Map.of(
            "message", "Todas las cachés han sido limpiadas",
            "status", "success"
        ));
    }

    @PostMapping("/clear/{cacheName}")
    @Operation(summary = "Limpiar una caché específica")
    public ResponseEntity<Map<String, String>> clearCache(
            @PathVariable String cacheName) {
        cacheService.clearCacheByName(cacheName);
        return ResponseEntity.ok(Map.of(
            "message", String.format("Caché '%s' ha sido limpiada", cacheName),
            "status", "success"
        ));
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas de caché")
    public ResponseEntity<CacheStats> getCacheStats() {
        CacheStats stats = cacheService.getCacheStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/keys/{pattern}")
    @Operation(summary = "Listar claves por patrón")
    public ResponseEntity<Map<String, Object>> getKeysByPattern(
            @PathVariable String pattern) {
        Map<String, Object> keys = cacheService.getKeysByPattern(pattern);
        return ResponseEntity.ok(keys);
    }

    @GetMapping("/info/{cacheName}")
    @Operation(summary = "Obtener información de una caché")
    public ResponseEntity<Map<String, Object>> getCacheInfo(
            @PathVariable String cacheName) {
        Map<String, Object> info = cacheService.getCacheInfo(cacheName);
        return ResponseEntity.ok(info);
    }
}