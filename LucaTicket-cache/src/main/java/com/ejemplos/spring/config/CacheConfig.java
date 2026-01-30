package com.ejemplos.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${app.cache.eventos.ttl:300}")
    private long eventosTtl;

    @Value("${app.cache.usuarios.ttl:1800}")
    private long usuariosTtl;

    @Value("${app.cache.compras.ttl:900}")
    private long comprasTtl;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(eventosTtl))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // Configuraciones específicas por caché
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        cacheConfigurations.put("eventos", 
            defaultConfig.entryTtl(Duration.ofSeconds(eventosTtl)));
        
        cacheConfigurations.put("usuarios", 
            defaultConfig.entryTtl(Duration.ofSeconds(usuariosTtl)));
        
        cacheConfigurations.put("compras", 
            defaultConfig.entryTtl(Duration.ofSeconds(comprasTtl)));
        
        cacheConfigurations.put("evento-detalle", 
            defaultConfig.entryTtl(Duration.ofSeconds(eventosTtl)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }
}