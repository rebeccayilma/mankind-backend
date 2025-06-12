package com.mankind.matrix_product_service.dto.returneditem;

import com.mankind.matrix_product_service.dto.product.ProductResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "DTO for returned item responses")
public class ReturnedItemResponseDTO {
    @Schema(description = "Unique identifier of the returned item", example = "1")
    private Long id;

    @Schema(description = "ID of the product being returned", example = "1")
    private Long productId;

    @Schema(description = "Product information")
    private ProductResponseDTO product;

    @Schema(description = "ID of the user who returned the product", example = "1")
    private Long userId;

    @Schema(description = "Reason for returning the product", example = "Product was damaged during shipping")
    private String reason;

    @Schema(description = "Date when the product was returned", example = "2023-01-01T10:00:00")
    private LocalDateTime returnDate;

    @Schema(description = "Status of the returned item", example = "PENDING")
    private String status;

    @Schema(description = "Timestamp when the returned item was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the returned item was last updated")
    private LocalDateTime updatedAt;
}