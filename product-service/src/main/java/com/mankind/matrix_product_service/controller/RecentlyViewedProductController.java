package com.mankind.matrix_product_service.controller;

import com.mankind.api.product.dto.recentlyviewed.RecentlyViewedProductResponseDTO;
import com.mankind.matrix_product_service.service.RecentlyViewedProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recently-viewed")
@RequiredArgsConstructor
@Tag(name = "Recently Viewed Products", description = "APIs for managing recently viewed products")
public class RecentlyViewedProductController {

    private final RecentlyViewedProductService recentlyViewedProductService;

    @Operation(summary = "Add product to recently viewed", description = "Adds a product to a user's recently viewed products list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully added to recently viewed",
                    content = @Content(schema = @Schema(implementation = RecentlyViewedProductResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<RecentlyViewedProductResponseDTO> addRecentlyViewedProduct(
            @Parameter(description = "ID of the user", required = true)
            @RequestParam Long userId,
            @Parameter(description = "ID of the product", required = true)
            @RequestParam Long productId) {
        return ResponseEntity.ok(recentlyViewedProductService.addRecentlyViewedProduct(userId, productId));
    }

    @Operation(summary = "Get recently viewed products", description = "Retrieves a paginated list of a user's recently viewed products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved recently viewed products",
                    content = @Content(schema = @Schema(implementation = RecentlyViewedProductResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<RecentlyViewedProductResponseDTO>> getRecentlyViewedProducts(
            @Parameter(description = "ID of the user", required = true)
            @RequestParam Long userId,
            @Parameter(description = "Pagination and sorting parameters")
            Pageable pageable) {
        return ResponseEntity.ok(recentlyViewedProductService.getRecentlyViewedProducts(userId, pageable));
    }

    @Operation(summary = "Remove product from recently viewed", description = "Removes a product from a user's recently viewed products list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product successfully removed from recently viewed"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "404", description = "Product not found in recently viewed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping
    public ResponseEntity<Void> removeRecentlyViewedProduct(
            @Parameter(description = "ID of the user", required = true)
            @RequestParam Long userId,
            @Parameter(description = "ID of the product", required = true)
            @RequestParam Long productId) {
        recentlyViewedProductService.removeRecentlyViewedProduct(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Clear recently viewed products", description = "Clears all recently viewed products for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Recently viewed products successfully cleared"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "404", description = "No recently viewed products found for user"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearRecentlyViewedProducts(
            @Parameter(description = "ID of the user", required = true)
            @RequestParam Long userId) {
        recentlyViewedProductService.clearRecentlyViewedProducts(userId);
        return ResponseEntity.noContent().build();
    }
}
