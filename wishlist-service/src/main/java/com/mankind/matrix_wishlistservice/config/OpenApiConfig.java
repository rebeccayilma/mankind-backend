package com.mankind.matrix_wishlistservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("wishlist-public")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI wishlistServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wishlist Service API")
                        .description("""
                            API documentation for the Mankind Matrix Wishlist Service.
                            This service provides endpoints for managing user wishlists, allowing users to:
                            - Add products to their wishlist
                            - Remove products from their wishlist
                            - View their wishlist
                            - Check if a product is in their wishlist
                            
                            All endpoints are prefixed with `/api/wishlist`.
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mankind Matrix Team")
                                .email("support@mankindmatrix.com")
                                .url("https://www.mankindmatrix.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                    new Server()
                        .url("http://localhost:8083")
                        .description("Local Development Server"),
                    new Server()
                        .url("https://api.mankindmatrix.com")
                        .description("Production Server")
                ))
                .components(new Components()
                    .addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT token for authentication")));
    }
}
