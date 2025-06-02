package com.mankind.matrix_cart_service.dto;

import com.mankind.matrix_cart_service.model.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private Long id;
    private Long userId;
    private String sessionId;
    private CartStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<CartItemResponseDto> cartItems = new ArrayList<>();

    // Calculated fields
    private Integer totalItems;
    private BigDecimal subtotal;

    // Helper method to calculate totals
    public void calculateTotals() {
        this.totalItems = cartItems.stream()
                .mapToInt(CartItemResponseDto::getQuantity)
                .sum();

        this.subtotal = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getPrice() * item.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
