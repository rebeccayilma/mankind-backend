package com.mankind.matrix_product_service.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "DTO for inventory status information")
public class InventoryStatusDTO {
    @Schema(description = "ID of the product", example = "1")
    private Long productId;

    @Schema(description = "Name of the product", example = "iPhone 13 Pro")
    private String productName;

    @Schema(description = "Available quantity in stock", example = "10.00")
    private BigDecimal availableQuantity;

    @Schema(description = "Reserved quantity", example = "2.00")
    private BigDecimal reservedQuantity;

    @Schema(description = "Sold quantity", example = "50.00")
    private BigDecimal soldQuantity;

    @Schema(description = "Total quantity (available + reserved)", example = "12.00")
    private BigDecimal totalQuantity;

    @Schema(description = "Current inventory status", example = "IN_STOCK", allowableValues = {"NO_INVENTORY", "OUT_OF_STOCK", "IN_STOCK"})
    private String status;
} 