package com.l7aur.authenticationmicroservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI authenticationAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Authentication Microservice")
                        .description("API documentation for the energy management application")
                        .version("1.0.0"))
                .servers(List.of(new Server().url("/auth").description("Authentication Microservice")));
    }
}
