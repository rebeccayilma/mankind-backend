package com.mankind.matrix_product_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mankind Matrix Product Service API")
                        .description("API for managing products in the Mankind Matrix system")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Mankind Team")
                                .email("support@mankind.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://mankind.com/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.mankind.com")
                                .description("Production Server")
                ));
    }
} 