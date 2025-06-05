package com.mankind.matrix_cart_service.controller;

import com.mankind.matrix_cart_service.dto.CartRequestDto;
import com.mankind.matrix_cart_service.dto.CartResponseDto;
import com.mankind.matrix_cart_service.model.CartStatus;
import com.mankind.matrix_cart_service.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Tag(name = "Cart API", description = "API for managing shopping carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{id}")
    @Operation(summary = "Get cart by ID", description = "Retrieves a cart by its ID")
    public ResponseEntity<CartResponseDto> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping("/user/{userId}/active")
    @Operation(summary = "Get active cart by user ID", description = "Retrieves the active cart for a specific user")
    public ResponseEntity<CartResponseDto> getActiveCartByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getActiveCartByUserId(userId));
    }

    @GetMapping("/session/{sessionId}/active")
    @Operation(summary = "Get active cart by session ID", description = "Retrieves the active cart for a specific session")
    public ResponseEntity<CartResponseDto> getActiveCartBySessionId(@PathVariable String sessionId) {
        return ResponseEntity.ok(cartService.getActiveCartBySessionId(sessionId));
    }

    @PostMapping
    @Operation(summary = "Create cart", description = "Creates a new cart")
    public ResponseEntity<CartResponseDto> createCart(@Valid @RequestBody CartRequestDto cartRequestDto) {
        return new ResponseEntity<>(cartService.createCart(cartRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update cart", description = "Updates an existing cart")
    public ResponseEntity<CartResponseDto> updateCart(
            @PathVariable Long id, 
            @Valid @RequestBody CartRequestDto cartRequestDto) {
        return ResponseEntity.ok(cartService.updateCart(id, cartRequestDto));
    }

    @PatchMapping("/{id}/status/{status}")
    @Operation(summary = "Update cart status", description = "Updates the status of an existing cart")
    public ResponseEntity<CartResponseDto> updateCartStatus(
            @PathVariable Long id, 
            @PathVariable CartStatus status) {
        return ResponseEntity.ok(cartService.updateCartStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete cart", description = "Deletes a cart")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
