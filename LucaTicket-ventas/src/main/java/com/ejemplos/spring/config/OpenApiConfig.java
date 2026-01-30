package com.ejemplos.spring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI lucaTicketOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("LucaTicket Ventas API")
                .description("API para el servicio de ventas de entradas de LucaTicket")
                .version("v1.0")
                .contact(new Contact()
                    .name("Equipo LucaTicket")
                    .email("soporte@lucaticket.com")
                    .url("https://www.lucaticket.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                .termsOfService("https://www.lucaticket.com/terms"));
    }
}