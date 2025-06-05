package com.mankind.matrix_cart_service.service;

import com.mankind.matrix_cart_service.dto.CartRequestDto;
import com.mankind.matrix_cart_service.dto.CartResponseDto;
import com.mankind.matrix_cart_service.model.CartStatus;

public interface CartService {

    /**
     * Get cart by ID
     * @param id the ID of the cart
     * @return the cart
     */
    CartResponseDto getCartById(Long id);

    /**
     * Get active cart for a specific user
     * @param userId the ID of the user
     * @return the active cart
     */
    CartResponseDto getActiveCartByUserId(Long userId);

    /**
     * Get active cart for a guest session
     * @param sessionId the session ID
     * @return the active cart
     */
    CartResponseDto getActiveCartBySessionId(String sessionId);

    /**
     * Create a new cart
     * @param cartRequestDto the cart data
     * @return the created cart
     */
    CartResponseDto createCart(CartRequestDto cartRequestDto);

    /**
     * Update an existing cart
     * @param id the ID of the cart to update
     * @param cartRequestDto the updated cart data
     * @return the updated cart
     */
    CartResponseDto updateCart(Long id, CartRequestDto cartRequestDto);

    /**
     * Update the status of a cart
     * @param id the ID of the cart to update
     * @param status the new status
     * @return the updated cart
     */
    CartResponseDto updateCartStatus(Long id, CartStatus status);

    /**
     * Delete a cart
     * @param id the ID of the cart to delete
     */
    void deleteCart(Long id);
}
