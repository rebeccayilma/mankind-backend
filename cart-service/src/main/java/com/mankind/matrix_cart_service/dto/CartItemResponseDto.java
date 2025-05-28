package com.mankind.matrix_cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDto {
    private Long id;
    private Long cartId;
    private Long productId;
    private Integer quantity;

    // Product information for display
    private String productName;
    private String productImageUrl;

    // Price tracking
    private BigDecimal priceAtAddition;
    private BigDecimal totalPrice;

    // Save for later functionality
    private boolean savedForLater;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
