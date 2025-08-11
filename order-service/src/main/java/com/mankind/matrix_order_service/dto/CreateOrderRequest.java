package com.mankind.matrix_order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request DTO for creating a new order")
public class CreateOrderRequest {
    @NotNull(message = "Shipping address ID is required")
    @Schema(description = "Shipping address ID", example = "1")
    private Long shippingAddressId;

    @NotNull(message = "Billing address ID is required")
    @Schema(description = "Billing address ID", example = "1")
    private Long billingAddressId;

    @Schema(description = "Coupon code to apply", example = "SAVE20")
    private String couponCode;

    @Schema(description = "Additional notes for the order")
    private String notes;
}
