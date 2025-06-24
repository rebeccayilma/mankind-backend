package com.mankind.matrix_cart_service.controller;

import com.mankind.matrix_cart_service.dto.CartItemRequestDto;
import com.mankind.matrix_cart_service.dto.CartItemResponseDto;
import com.mankind.matrix_cart_service.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
@Tag(name = "Cart Item API", description = "API for managing cart items")
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping
    @Operation(summary = "Get all cart items", description = "Retrieves a list of all cart items")
    public ResponseEntity<List<CartItemResponseDto>> getAllCartItems() {
        return ResponseEntity.ok(cartItemService.getAllCartItems());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cart item by ID", description = "Retrieves a cart item by its ID")
    public ResponseEntity<CartItemResponseDto> getCartItemById(@PathVariable Long id) {
        return ResponseEntity.ok(cartItemService.getCartItemById(id));
    }

    @GetMapping("/cart/{cartId}")
    @Operation(summary = "Get cart items by cart ID", description = "Retrieves all cart items for a specific cart")
    public ResponseEntity<List<CartItemResponseDto>> getCartItemsByCartId(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartItemService.getCartItemsByCartId(cartId));
    }

    @PostMapping
    @Operation(summary = "Create cart item", description = "Creates a new cart item")
    public ResponseEntity<CartItemResponseDto> createCartItem(@Valid @RequestBody CartItemRequestDto cartItemRequestDto) {
        return new ResponseEntity<>(cartItemService.createCartItem(cartItemRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update cart item", description = "Updates an existing cart item")
    public ResponseEntity<CartItemResponseDto> updateCartItem(
            @PathVariable Long id, 
            @Valid @RequestBody CartItemRequestDto cartItemRequestDto) {
        return ResponseEntity.ok(cartItemService.updateCartItem(id, cartItemRequestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete cart item", description = "Deletes a cart item")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cart/{cartId}")
    @Operation(summary = "Delete cart items by cart ID", description = "Deletes all cart items for a specific cart")
    public ResponseEntity<Void> deleteCartItemsByCartId(@PathVariable Long cartId) {
        cartItemService.deleteCartItemsByCartId(cartId);
        return ResponseEntity.noContent().build();
    }
}
