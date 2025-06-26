package com.mankind.matrix_payment_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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
    public OpenAPI paymentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mankind Matrix Payment Service API")
                        .description("""
                            API documentation for the Mankind Matrix Payment Service.
                            This service provides endpoints for managing payments, allowing users to:
                            - Process payments
                            - Manage payment methods
                            - View payment history
                            - Handle refunds

                            All endpoints are prefixed with `/payments`.
                            
                            Swagger UI: http://localhost:8084/swagger-ui
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
                                .url("http://localhost:8084")
                                .description("Local Development Server (Direct Access)"),
                        new Server()
                                .url("http://localhost:8085/api/v1/payments")
                                .description("Gateway Server (Through Gateway)")
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
