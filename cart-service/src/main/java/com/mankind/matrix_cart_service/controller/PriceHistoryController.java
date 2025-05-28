package com.mankind.matrix_cart_service.controller;

import com.mankind.matrix_cart_service.dto.PriceHistoryResponseDto;
import com.mankind.matrix_cart_service.service.PriceHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/price-history")
@RequiredArgsConstructor
@Tag(name = "Price History API", description = "API for retrieving price history")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    @GetMapping("/cart-item/{cartItemId}")
    @Operation(summary = "Get price history by cart item ID", description = "Retrieves price history for a specific cart item")
    public ResponseEntity<List<PriceHistoryResponseDto>> getPriceHistoryByCartItemId(@PathVariable Long cartItemId) {
        return ResponseEntity.ok(priceHistoryService.getPriceHistoryByCartItemId(cartItemId));
    }
}