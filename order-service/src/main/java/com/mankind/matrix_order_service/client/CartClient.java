package com.mankind.matrix_order_service.client;

import com.mankind.api.cart.dto.CartResponseDTO;
import com.mankind.matrix_order_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;

@FeignClient(
    name = "cart-service",
    url = "${CART_SERVICE_URL:http://localhost:8082}",
    configuration = FeignConfig.class
)
public interface CartClient {
    @GetMapping("/cart")
    CartResponseDTO getCurrentUserCart();

    @GetMapping("/cart/{cartId}")
    CartResponseDTO getCartById(@PathVariable("cartId") Long cartId);

    @PatchMapping("/cart/{cartId}/status/CONVERTED")
    CartResponseDTO updateCartStatusToConverted(@PathVariable("cartId") Long cartId);
}
