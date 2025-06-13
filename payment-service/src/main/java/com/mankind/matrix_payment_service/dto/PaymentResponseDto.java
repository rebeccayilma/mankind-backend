package com.mankind.matrix_payment_service.dto;

import com.mankind.matrix_payment_service.model.PaymentGateway;
import com.mankind.matrix_payment_service.model.PaymentMethod;
import com.mankind.matrix_payment_service.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for payment response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    
    private UUID id;
    private String orderId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private PaymentGateway paymentGateway;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields that might be useful for the client
    private String receiptUrl;
    private String description;
    private String errorMessage;
}