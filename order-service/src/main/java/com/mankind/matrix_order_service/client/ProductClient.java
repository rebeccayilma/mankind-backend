package com.mankind.matrix_order_service.client;

import com.mankind.api.product.dto.product.ProductResponseDTO;
import com.mankind.api.product.dto.inventory.InventoryResponseDTO;
import com.mankind.matrix_order_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(
    name = "product-service",
    url = "${PRODUCT_SERVICE_URL:http://localhost:8080}",
    configuration = FeignConfig.class
)
public interface ProductClient {
    @GetMapping("/products/{id}")
    ProductResponseDTO getProductById(@PathVariable Long id);

    @GetMapping("/inventory/{productId}")
    InventoryResponseDTO getInventoryByProductId(@PathVariable Long productId);

    @PostMapping("/inventory/{productId}/sold")
    InventoryResponseDTO markProductAsSold(
            @PathVariable Long productId,
            @RequestParam BigDecimal quantity);
}
