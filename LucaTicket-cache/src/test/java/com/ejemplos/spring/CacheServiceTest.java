package com.ejemplos.spring;


import com.ejemplos.spring.service.CacheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CacheServiceTest {

    @Autowired
    private CacheService cacheService;

    @Test
    void testCacheServiceIsAvailable() {
        assertThat(cacheService).isNotNull();
        assertThat(cacheService.isCacheEnabled()).isTrue();
    }

    @Test
    void testClearCache() {
        // Test que no lance excepciones
        cacheService.clearCacheByName("test-cache");
    }
}