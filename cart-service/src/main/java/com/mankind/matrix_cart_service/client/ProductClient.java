package com.mankind.matrix_cart_service.client;

import com.mankind.api.product.dto.product.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mankind.api.product.dto.inventory.InventoryResponseDTO;

@FeignClient(name = "product-service", url = "${product-service.url:http://localhost:8080}")
public interface ProductClient {
    @GetMapping("/products/{id}")
    ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id);

    @GetMapping("/inventory/{productId}")
    ResponseEntity<InventoryResponseDTO> getInventoryByProductId(@PathVariable Long productId);
} 