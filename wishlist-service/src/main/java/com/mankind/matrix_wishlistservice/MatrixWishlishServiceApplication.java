package com.mankind.matrix_wishlistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Wishlist Service API",
        version = "1.0.0",
        description = "API documentation for the Mankind Matrix Wishlist Service"
    )
)
public class MatrixWishlishServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatrixWishlishServiceApplication.class, args);
	}

}
