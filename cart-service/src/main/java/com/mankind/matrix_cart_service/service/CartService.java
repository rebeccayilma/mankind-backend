package com.mankind.matrix_cart_service.service;

import com.mankind.matrix_cart_service.dto.CartRequestDto;
import com.mankind.matrix_cart_service.dto.CartResponseDto;
import com.mankind.matrix_cart_service.model.CartStatus;

import java.util.List;

public interface CartService {
    
    /**
     * Get all carts
     * @return list of all carts
     */
    List<CartResponseDto> getAllCarts();
    
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
     * Get all carts for a specific user
     * @param userId the ID of the user
     * @return list of carts for the user
     */
    List<CartResponseDto> getCartsByUserId(Long userId);
    
    /**
     * Get all carts for a specific session
     * @param sessionId the session ID
     * @return list of carts for the session
     */
    List<CartResponseDto> getCartsBySessionId(String sessionId);
    
    /**
     * Get all carts with a specific status
     * @param status the status of the cart
     * @return list of carts with the status
     */
    List<CartResponseDto> getCartsByStatus(CartStatus status);
    
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
    
    /**
     * Delete all carts for a specific user
     * @param userId the ID of the user
     */
    void deleteCartsByUserId(Long userId);
    
    /**
     * Delete all carts for a specific session
     * @param sessionId the session ID
     */
    void deleteCartsBySessionId(String sessionId);
}