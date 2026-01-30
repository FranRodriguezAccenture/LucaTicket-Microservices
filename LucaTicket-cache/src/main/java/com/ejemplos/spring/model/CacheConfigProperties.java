package com.ejemplos.spring.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.cache")
@Data
public class CacheConfigProperties {
    private int defaultTtl;
    private CacheConfig eventos;
    private CacheConfig usuarios;
    private CacheConfig compras;
    
    @Data
    public static class CacheConfig {
        private int ttl;
        private int maxSize;
    }
}