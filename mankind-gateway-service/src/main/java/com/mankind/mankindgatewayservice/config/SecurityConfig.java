package com.mankind.mankindgatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(ex -> ex
                        // Public endpoints (no authentication required)
                        .pathMatchers("/", "/index.html").permitAll()
                        .pathMatchers("/actuator/**").permitAll()  // Allow public access to actuator endpoints
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        
                        // Product service - public read access, protected write access
                        .pathMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()  // All GET requests to products
                        
                        // Protected product endpoints (require authentication for write operations)
                        .pathMatchers(HttpMethod.POST, "/api/v1/products/**").authenticated()
                        .pathMatchers(HttpMethod.PUT, "/api/v1/products/**").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/products/**").authenticated()
                        .pathMatchers(HttpMethod.PATCH, "/api/v1/products/**").authenticated()
                        
                        // Protected endpoints (authentication required)
                        .pathMatchers("/api/v1/users/**").authenticated()
                        .pathMatchers("/api/v1/cart/**").authenticated()
                        .pathMatchers("/api/v1/wishlist/**").authenticated()
                        .pathMatchers("/api/v1/payments/**").authenticated()
                        .pathMatchers("/api/v1/admin/payments/**").authenticated()
                        .pathMatchers("/api/v1/notifications/**").authenticated()
                        
                        // Default: require authentication for any other endpoints
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
