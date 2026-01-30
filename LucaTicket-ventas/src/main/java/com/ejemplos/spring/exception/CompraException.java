package com.ejemplos.spring.exception;

public class CompraException extends RuntimeException {
    
    private final String codigoError;
    
    public CompraException(String mensaje) {
        super(mensaje);
        this.codigoError = "COMPRA_ERROR_001";
    }
    
    public CompraException(String mensaje, String codigoError) {
        super(mensaje);
        this.codigoError = codigoError;
    }
    
    public CompraException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = "COMPRA_ERROR_002";
    }
    
    public String getCodigoError() {
        return codigoError;
    }
}