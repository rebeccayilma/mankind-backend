package com.mankind.matrix_payment_service.dto;

import com.mankind.matrix_payment_service.model.LogType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for payment log response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLogResponseDto {
    
    private UUID id;
    private UUID paymentId;
    private LogType logType;
    private String logMessage;
    private LocalDateTime createdAt;
    
    // Additional fields for admin view
    private String orderId;
    private String userId;
}