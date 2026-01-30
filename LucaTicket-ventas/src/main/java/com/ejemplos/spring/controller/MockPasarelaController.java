package com.ejemplos.spring.controller;

import com.ejemplos.spring.model.dto.PasarelaRequestDTO;
import com.ejemplos.spring.model.dto.PasarelaResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pasarela")
public class MockPasarelaController {
    
	@PostMapping("/compra")
	public PasarelaResponseDTO mockCompra(@RequestBody PasarelaRequestDTO request) {
	    PasarelaResponseDTO response = new PasarelaResponseDTO();
	    String numeroTarjeta = request.getNumeroTarjeta();
	    String cvv = request.getCvv();
	    String mes = request.getMesCaducidad();
	    String year = request.getYearCaducidad();
	    String nombre = request.getNombreTitular();
	    
	    // IMPORTANTE: Evaluar en orden específico
	    if (numeroTarjeta.contains("1111")) {
	        response.setCodigo("400.0001");
	        response.setDescripcion("No hay fondos suficientes en la cuenta, lechón");
	    } else if (numeroTarjeta.contains("2222")) {
	        response.setCodigo("400.0003");
	        response.setDescripcion("El número de la tarjeta no es válido, animal");
	    } else if (cvv == null || cvv.length() != 3) {
	        response.setCodigo("400.0004");
	        response.setDescripcion("El formato del cvv no es válido, idiota");
	    } else if (year == null || year.length() != 4) {
	        response.setCodigo("400.0006");
	        response.setDescripcion("El año de caducidad no es correcto, majete");
	    } else if (mes == null || Integer.parseInt(mes) < 1 || Integer.parseInt(mes) > 12) {
	        response.setCodigo("400.0005");
	        response.setDescripcion("El mes de caducidad no es correcto, vago");
	    } else {
	        // AHORA year tiene 4 dígitos seguro
	        int tarjetaYear = Integer.parseInt(year);
	        int tarjetaMes = Integer.parseInt(mes);
	        int currentYear = java.time.Year.now().getValue();
	        int currentMonth = java.time.LocalDate.now().getMonthValue();
	        
	        if (tarjetaYear < currentYear || (tarjetaYear == currentYear && tarjetaMes < currentMonth)) {
	            response.setCodigo("400.0007");
	            response.setDescripcion("La fecha de caducidad debe ser posterior al día actual, lechón");
	        } else if (nombre == null || !nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
	            response.setCodigo("400.0008");
	            response.setDescripcion("El formato del nombre no es correcto, animal");
	        } else if (numeroTarjeta.contains("9999")) {
	            response.setCodigo("500.0001");
	            response.setDescripcion("El sistema se encuentra inestable, majete");
	        } else {
	            response.setCodigo("200.0001");
	            response.setDescripcion("Transacción correcta, lechón. ¡Disfruta del evento!");
	        }
	    }
	    
	    // SIEMPRE copiar datos (importante)
	    response.setNombreTitular(request.getNombreTitular());
	    response.setNumeroTarjeta(request.getNumeroTarjeta());
	    response.setMesCaducidad(request.getMesCaducidad());
	    response.setYearCaducidad(request.getYearCaducidad());
	    response.setCvv(request.getCvv());
	    response.setEmisor(request.getEmisor());
	    response.setConcepto(request.getConcepto());
	    response.setCantidad(request.getCantidad());
	    
	    return response;
	}
}