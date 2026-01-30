package com.ejemplos.spring.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheStats {
    private String totalConnections;
    private String memoryUsed;
    private String hits;
    private String misses;
    private String uptime;
    private int numberOfKeys;
    private double hitRate;
    
    public double getHitRate() {
        try {
            long hitsLong = Long.parseLong(hits);
            long missesLong = Long.parseLong(misses);
            long total = hitsLong + missesLong;
            return total > 0 ? (double) hitsLong / total * 100 : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}