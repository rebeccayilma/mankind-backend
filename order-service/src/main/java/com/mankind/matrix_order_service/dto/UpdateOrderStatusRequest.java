package com.mankind.matrix_order_service.dto;

import com.mankind.matrix_order_service.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request DTO for updating order status")
public class UpdateOrderStatusRequest {
    
    @NotNull(message = "Status is required")
    @Schema(description = "New order status", example = "CONFIRMED", 
            allowableValues = {"PENDING", "CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"})
    private Order.OrderStatus status;
    
    @Schema(description = "Optional notes about the status change", example = "Order confirmed by admin")
    private String notes;
}
