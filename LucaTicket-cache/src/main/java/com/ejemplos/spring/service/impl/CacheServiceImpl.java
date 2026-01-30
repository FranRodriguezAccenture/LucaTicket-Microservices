package com.ejemplos.spring.service.impl;


import com.ejemplos.spring.model.CacheStats;
import com.ejemplos.spring.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void clearAllCaches() {
        log.info("Limpiando todas las cachés");
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(
                        cacheManager.getCache(cacheName)).clear());
    }

    @Override
    public void clearCacheByName(String cacheName) {
        log.info("Limpiando caché: {}", cacheName);
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    @Override
    public CacheStats getCacheStats() {
        try {
            var connection = redisConnectionFactory.getConnection();
            var info = connection.info();
            
            CacheStats stats = new CacheStats();
            stats.setTotalConnections(info.getProperty("connected_clients"));
            stats.setMemoryUsed(info.getProperty("used_memory_human"));
            stats.setHits(info.getProperty("keyspace_hits"));
            stats.setMisses(info.getProperty("keyspace_misses"));
            stats.setUptime(info.getProperty("uptime_in_seconds"));
            stats.setNumberOfKeys(connection.keys("*".getBytes()).size());
            
            connection.close();
            return stats;
            
        } catch (Exception e) {
            log.error("Error obteniendo estadísticas de Redis", e);
            return new CacheStats();
        }
    }

    @Override
    public Map<String, Object> getKeysByPattern(String pattern) {
        Map<String, Object> result = new HashMap<>();
        try {
            var connection = redisConnectionFactory.getConnection();
            var keys = connection.keys((pattern + "*").getBytes());
            
            List<String> keyList = new ArrayList<>();
            for (byte[] key : keys) {
                keyList.add(new String(key));
            }
            
            result.put("pattern", pattern);
            result.put("count", keyList.size());
            result.put("keys", keyList);
            
            connection.close();
        } catch (Exception e) {
            log.error("Error obteniendo claves por patrón", e);
        }
        return result;
    }

    @Override
    public Map<String, Object> getCacheInfo(String cacheName) {
        Map<String, Object> info = new HashMap<>();
        info.put("cacheName", cacheName);
        info.put("exists", cacheManager.getCache(cacheName) != null);
        info.put("timestamp", System.currentTimeMillis());
        return info;
    }

    @Override
    public void evictKey(String cacheName, String key) {
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
            log.info("Clave '{}' eliminada de la caché '{}'", key, cacheName);
        }
    }

    @Override
    public void preloadCache(String cacheName) {
        log.info("Precargando caché: {}", cacheName);
        // Implementar lógica de precarga según el tipo de caché
    }

    @Override
    public boolean isCacheEnabled() {
        return cacheManager != null;
    }
}