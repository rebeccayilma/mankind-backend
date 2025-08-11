package com.mankind.matrix_order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for order details")
public class OrderResponseDTO {
    @Schema(description = "Order ID", example = "1")
    private Long id;

    @Schema(description = "Unique order number", example = "ORD-20241201-143022-12345")
    private String orderNumber;

    @Schema(description = "User ID", example = "123")
    private Long userId;

    @Schema(description = "Cart ID", example = "1")
    private Long cartId;

    @Schema(description = "Order status", example = "PENDING")
    private String status;

    @Schema(description = "Payment status", example = "PENDING")
    private String paymentStatus;

    @Schema(description = "Total amount before discounts", example = "100.00")
    private BigDecimal totalAmount;

    @Schema(description = "Total discount amount", example = "20.00")
    private BigDecimal discountAmount;

    @Schema(description = "Final amount after discounts", example = "80.00")
    private BigDecimal finalAmount;

    @Schema(description = "Payment ID", example = "pay_123456789")
    private String paymentId;

    @Schema(description = "Shipping address ID", example = "1")
    private Long shippingAddressId;

    @Schema(description = "Billing address ID", example = "1")
    private Long billingAddressId;

    @Schema(description = "Order notes")
    private String notes;

    @Schema(description = "Order items")
    private List<OrderItemDTO> items;

    @Schema(description = "Applied coupon")
    private OrderCouponDTO appliedCoupon;

    @Schema(description = "Order creation date")
    private LocalDateTime createdAt;

    @Schema(description = "Order last update date")
    private LocalDateTime updatedAt;
}
