package com.mankind.matrix_payment_service.dto;

import com.mankind.matrix_payment_service.model.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a payment intent
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    // userId will be extracted from security context for authenticated users
    private String userId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotBlank(message = "Currency is required")
    private String currency;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
    
    // Optional description for the payment
    private String description;
    
    // Optional metadata as JSON string
    private String metadata;
}