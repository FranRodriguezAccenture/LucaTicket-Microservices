package com.ejemplos.spring;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
public class ApiGatewayController {
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    // Simulación de Circuit Breaker manual
    private final Map<String, CircuitBreakerState> circuitStates = new ConcurrentHashMap<>();
    
    static class CircuitBreakerState {
        AtomicInteger failures = new AtomicInteger(0);
        AtomicInteger successes = new AtomicInteger(0);
        boolean isOpen = false;
        long openedAt = 0;
        
        boolean shouldAllowRequest() {
            if (!isOpen) return true;
            
            // Si está abierto, verificar si ha pasado el tiempo de espera (10 segundos)
            if (System.currentTimeMillis() - openedAt > 10000) {
                isOpen = false; // Poner en estado HALF_OPEN
                failures.set(0);
                return true;
            }
            return false;
        }
        
        void recordFailure() {
            failures.incrementAndGet();
            if (failures.get() >= 3) { // Después de 3 fallos, abrir circuito
                isOpen = true;
                openedAt = System.currentTimeMillis();
            }
        }
        
        void recordSuccess() {
            successes.incrementAndGet();
            failures.set(0);
            if (successes.get() >= 2) { // 2 éxitos seguidos, cerrar circuito
                isOpen = false;
                successes.set(0);
            }
        }
    }
    
    @GetMapping("/eventos")
    public ResponseEntity<?> getEventos() {
        return withCircuitBreaker("eventos", () -> {
            String eventosUrl = "http://localhost:8081/eventos";
            return proxyRequest(eventosUrl, HttpMethod.GET, null);
        });
    }
    
    @PostMapping("/ventas/compra")
    public ResponseEntity<?> comprarEntrada(@RequestBody Object compraRequest) {
        return withCircuitBreaker("ventas", () -> {
            String ventasUrl = "http://localhost:8082/api/ventas/compra";
            return proxyRequest(ventasUrl, HttpMethod.POST, compraRequest);
        });
    }
    
    // Método genérico con Circuit Breaker
    private ResponseEntity<?> withCircuitBreaker(String serviceName, ServiceCall serviceCall) {
        CircuitBreakerState state = circuitStates.computeIfAbsent(
            serviceName, k -> new CircuitBreakerState()
        );
        
        // Verificar si el circuito está abierto
        if (!state.shouldAllowRequest()) {
            return createFallbackResponse(serviceName + " Service - Circuit Breaker OPEN");
        }
        
        try {
            ResponseEntity<?> response = serviceCall.execute();
            state.recordSuccess();
            return response;
            
        } catch (Exception e) {
            state.recordFailure();
            return createFallbackResponse(serviceName + " Service - " + e.getMessage());
        }
    }
    
    @FunctionalInterface
    private interface ServiceCall {
        ResponseEntity<?> execute() throws Exception;
    }
    
    private ResponseEntity<?> createFallbackResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        response.put("circuitBreaker", true);
        
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(response);
    }
    
    // Método proxy (igual que antes)
    private ResponseEntity<?> proxyRequest(String targetUrl, HttpMethod method, Object body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<?> entity = new HttpEntity<>(body, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                targetUrl, 
                method, 
                entity, 
                String.class
            );
            
            return ResponseEntity
                .status(response.getStatusCode())
                .headers(response.getHeaders())
                .body(response.getBody());
            
        } catch (Exception e) {
            throw new RuntimeException("Error calling service: " + e.getMessage(), e);
        }
    }
    
    // Endpoint para ver estado de Circuit Breakers
    @GetMapping("/circuitbreaker/status")
    public Map<String, Object> getCircuitBreakerStatus() {
        Map<String, Object> status = new HashMap<>();
        
        if (circuitStates.isEmpty()) {
            circuitStates.put("eventos", new CircuitBreakerState());
            circuitStates.put("ventas", new CircuitBreakerState());
        }
        
        circuitStates.forEach((service, state) -> {
            Map<String, Object> serviceStatus = new HashMap<>();
            serviceStatus.put("state", state.isOpen ? "OPEN" : "CLOSED");
            serviceStatus.put("failures", state.failures.get());
            serviceStatus.put("successes", state.successes.get());
            serviceStatus.put("openedAt", state.openedAt > 0 ? state.openedAt : null);
            serviceStatus.put("currentTime", System.currentTimeMillis());
            
            status.put(service, serviceStatus);
        });
        
        // Añadir metadata
        status.put("timestamp", System.currentTimeMillis());
        status.put("totalServices", circuitStates.size());
        
        return status;
    }
}