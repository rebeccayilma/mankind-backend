package com.mankind.matrix_product_service.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO for marking a product as out of stock")
public class OutOfStockRequestDTO {
    @Schema(description = "Reason for marking the product as out of stock", example = "Discontinued by manufacturer", required = false)
    private String reason;
}