package com.mankind.matrix_cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private String productDescription;
    private Integer quantity;
    private double price;
    private double subtotal;
} 