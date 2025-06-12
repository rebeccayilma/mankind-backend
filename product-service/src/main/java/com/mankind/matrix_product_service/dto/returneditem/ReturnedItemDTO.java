package com.mankind.matrix_product_service.dto.returneditem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "DTO for returned item operations (create/update)")
public class ReturnedItemDTO {
    @NotNull(message = "Product ID is required")
    @Schema(description = "ID of the product being returned", example = "1", required = true)
    private Long productId;

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user who returned the product", example = "1", required = true)
    private Long userId;

    @Size(max = 2000, message = "Reason cannot exceed 2000 characters")
    @Schema(description = "Reason for returning the product", example = "Product was damaged during shipping")
    private String reason;

    @NotNull(message = "Return date is required")
    @Schema(description = "Date when the product was returned", example = "2023-01-01T10:00:00", required = true)
    private LocalDateTime returnDate;

    @NotBlank(message = "Status is required")
    @Size(min = 2, max = 50, message = "Status must be between 2 and 50 characters")
    @Schema(description = "Status of the returned item", example = "PENDING", required = true)
    private String status;
}