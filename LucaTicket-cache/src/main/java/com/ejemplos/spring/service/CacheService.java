package com.ejemplos.spring.service;

package com.ejemplos.spring.service;

import com.ejemplos.spring.model.CacheStats;

import java.util.Map;

public interface CacheService {
    
    void clearAllCaches();
    void clearCacheByName(String cacheName);
    CacheStats getCacheStats();
    Map<String, Object> getKeysByPattern(String pattern);
    Map<String, Object> getCacheInfo(String cacheName);
    void evictKey(String cacheName, String key);
    void preloadCache(String cacheName);
    boolean isCacheEnabled();
}