package com.mankind.matrix_wishlistservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.mankind.matrix_wishlistservice.dto.SuccessResponse;
import com.mankind.matrix_wishlistservice.exception.ErrorResponse;
import com.mankind.matrix_wishlistservice.model.WishlistItem;
import com.mankind.matrix_wishlistservice.service.WishlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist Management", description = "API endpoints for managing user wishlists")
public class WishlistController {

    private final WishlistService service;

    public WishlistController(WishlistService service) {
        this.service = service;
    }

    @Operation(summary = "Add item to wishlist", description = "Adds a product to a user's wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item added successfully", 
                    content = @Content(schema = @Schema(implementation = WishlistItem.class))),
        @ApiResponse(responseCode = "400", description = "Item already in wishlist", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public WishlistItem add(
            @Parameter(description = "ID of the user") @RequestParam Long userId, 
            @Parameter(description = "ID of the product to add") @RequestParam Long productId,
            @Parameter(description = "Name of the product") @RequestParam(required = false) String name,
            @Parameter(description = "Brand of the product") @RequestParam(required = false) String brand,
            @Parameter(description = "Price of the product") @RequestParam(required = false) java.math.BigDecimal price,
            @Parameter(description = "Image URL of the product") @RequestParam(required = false) String imageUrl) {
        return service.addItem(userId, productId, name, brand, price, imageUrl);
    }

    @Operation(summary = "Get user's wishlist", description = "Returns all items in a user's wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wishlist retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found or wishlist is empty",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{userId}")
    public List<WishlistItem> get(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        return service.getUserWishlist(userId);
    }

    @Operation(summary = "Remove item from wishlist", description = "Removes a product from a user's wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item removed successfully", 
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found, no wishlist items available, or product not in wishlist",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public SuccessResponse delete(
            @Parameter(description = "ID of the user") @RequestParam Long userId, 
            @Parameter(description = "ID of the product to remove") @RequestParam Long productId) {
        service.removeItem(userId, productId);
        return new SuccessResponse("Item successfully removed from wishlist", 200);
    }

    @Operation(summary = "Check if item is in wishlist", description = "Checks if a product is in a user's wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item is in wishlist"),
        @ApiResponse(responseCode = "404", description = "User not found, no wishlist items available, or product not in wishlist",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/check")
    public boolean check(
            @Parameter(description = "ID of the user") @RequestParam Long userId, 
            @Parameter(description = "ID of the product to check") @RequestParam Long productId) {
        return service.isInWishlist(userId, productId);
    }
}
