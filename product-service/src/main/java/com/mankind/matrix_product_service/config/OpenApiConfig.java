package com.mankind.matrix_product_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mankind Matrix Product Service API")
                        .description("""
                            API documentation for the Mankind Matrix Product Service.
                            This service provides endpoints for managing products, allowing users to:
                            - Browse and search products
                            - View product details
                            - Manage product inventory
                            - Handle product returns

                            All endpoints are prefixed with `/api/products`.
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mankind Matrix Team")
                                .email("support@mankindmatrix.com")
                                .url("https://www.mankindmatrix.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://www.mankindmatrix.com/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.mankindmatrix.com")
                                .description("Production Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token for authentication. Include the token with the Bearer prefix, e.g. 'Bearer eyJhbGciOiJIUzI1NiJ9...'")));
    }
}
