package com.mankind.matrix_cart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.mankind.matrix_cart_service.client")
public class MankindMatrixCartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MankindMatrixCartServiceApplication.class, args);
    }

}