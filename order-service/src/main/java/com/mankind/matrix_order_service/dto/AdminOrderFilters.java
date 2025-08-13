package com.mankind.matrix_order_service.dto;

import com.mankind.matrix_order_service.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Schema(description = "Filters for admin order queries")
public class AdminOrderFilters {
    
    @Schema(description = "Order status filter", example = "PENDING", allowableValues = {"PENDING", "CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"})
    private Order.OrderStatus status;
    
    @Schema(description = "Start date for created date range filter (inclusive)", example = "2024-01-01T00:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtFrom;
    
    @Schema(description = "End date for created date range filter (inclusive)", example = "2024-12-31T23:59:59")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtTo;
    
    @Schema(description = "Payment status filter", example = "PAID", allowableValues = {"PENDING", "PAID", "FAILED", "REFUNDED", "PARTIALLY_REFUNDED"})
    private Order.PaymentStatus paymentStatus;
    
    @Schema(description = "Order number filter (partial match)", example = "ORD-2024")
    private String orderNumber;
}
