package com.mankind.matrix_wishlistservice.client;

import com.mankind.api.product.dto.product.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductClient {
    @GetMapping("/api/v1/products/{id}")
    ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id);
}
