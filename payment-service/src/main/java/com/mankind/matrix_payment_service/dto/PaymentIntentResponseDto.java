package com.mankind.matrix_payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for Stripe payment intent response
 * This is returned to the client for frontend processing with Stripe Elements
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentResponseDto {
    
    // The payment ID in our system
    private UUID paymentId;
    
    // Stripe payment intent ID
    private String paymentIntentId;
    
    // Client secret used by Stripe.js to complete the payment
    private String clientSecret;
    
    // The public key to be used with Stripe.js
    private String publicKey;
    
    // Payment status in our system
    private String status;
    
    // Optional error message
    private String errorMessage;
}