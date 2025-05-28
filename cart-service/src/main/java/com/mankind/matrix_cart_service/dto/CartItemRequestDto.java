package com.mankind.matrix_cart_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDto {
    @NotNull(message = "Cart ID cannot be null")
    private Long cartId;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than zero")
    private Integer quantity;

    // Product information for display
    @Size(max = 255, message = "Product name cannot exceed 255 characters")
    private String productName;

    @Size(max = 1000, message = "Product image URL cannot exceed 1000 characters")
    private String productImageUrl;

    // Price tracking
    @NotNull(message = "Price at addition cannot be null")
    private BigDecimal priceAtAddition;

    // Save for later functionality
    private Boolean savedForLater;
}
