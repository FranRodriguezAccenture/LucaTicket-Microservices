package com.ejemplos.spring.service.metrics;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheMetricsService {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Scheduled(fixedDelay = 60000) // Cada minuto
    public void logCacheMetrics() {
        try {
            var connection = redisConnectionFactory.getConnection();
            var info = connection.info("stats");
            
            long hits = Long.parseLong(info.getProperty("keyspace_hits"));
            long misses = Long.parseLong(info.getProperty("keyspace_misses"));
            long total = hits + misses;
            double hitRate = total > 0 ? (double) hits / total * 100 : 0;
            
            log.info("=== MÉTRICAS DE CACHÉ ===");
            log.info("Hits: {}, Misses: {}, Total: {}", hits, misses, total);
            log.info("Hit Rate: {:.2f}%", hitRate);
            log.info("Memoria usada: {}", info.getProperty("used_memory_human"));
            log.info("=========================");
            
            connection.close();
        } catch (Exception e) {
            log.warn("No se pudieron obtener métricas de Redis: {}", e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 3 * * ?") // Cada día a las 3 AM
    public void cleanExpiredKeys() {
        log.info("Ejecutando limpieza de claves expiradas...");
        // Redis limpia automáticamente las claves expiradas
    }
}