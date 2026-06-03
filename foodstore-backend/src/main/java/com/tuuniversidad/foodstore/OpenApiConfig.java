package com.tuuniversidad.foodstore;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI foodStoreOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Food Store API")
                        .description("API REST - Sistema de Gestion de Pedidos de Comida")
                        .version("1.0.0"));
    }
}
