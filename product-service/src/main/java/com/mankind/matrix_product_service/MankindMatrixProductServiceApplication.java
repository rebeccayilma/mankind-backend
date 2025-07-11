package com.mankind.matrix_product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class MankindMatrixProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MankindMatrixProductServiceApplication.class, args);
    }

}
