package com.mankind.matrix_wishlistservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                        .description("API documentation for the Mankind Matrix Wishlist Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mankind Matrix Team")
                                .email("support@mankindmatrix.com")
                                .url("https://www.mankindmatrix.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
