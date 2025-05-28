package com.mankind.matrix_cart_service.dto;

import com.mankind.matrix_cart_service.model.CartStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDto {
    private Long userId;
    
    @Size(max = 100, message = "Session ID cannot exceed 100 characters")
    private String sessionId;
    
    private CartStatus status;
}