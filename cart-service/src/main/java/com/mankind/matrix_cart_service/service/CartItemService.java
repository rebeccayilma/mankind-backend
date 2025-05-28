package com.mankind.matrix_cart_service.service;

import com.mankind.matrix_cart_service.dto.CartItemRequestDto;
import com.mankind.matrix_cart_service.dto.CartItemResponseDto;
import com.mankind.matrix_cart_service.model.Cart;

import java.util.List;

public interface CartItemService {

    /**
     * Get all cart items
     * @return list of all cart items
     */
    List<CartItemResponseDto> getAllCartItems();

    /**
     * Get cart item by ID
     * @param id the ID of the cart item
     * @return the cart item
     */
    CartItemResponseDto getCartItemById(Long id);

    /**
     * Get all cart items for a specific cart
     * @param cartId the ID of the cart
     * @return list of cart items for the cart
     */
    List<CartItemResponseDto> getCartItemsByCartId(Long cartId);

    /**
     * Get all cart items for a specific cart
     * @param cart the cart
     * @return list of cart items for the cart
     */
    List<CartItemResponseDto> getCartItemsByCart(Cart cart);

    /**
     * Create a new cart item
     * @param cartItemRequestDto the cart item data
     * @return the created cart item
     */
    CartItemResponseDto createCartItem(CartItemRequestDto cartItemRequestDto);

    /**
     * Update an existing cart item
     * @param id the ID of the cart item to update
     * @param cartItemRequestDto the updated cart item data
     * @return the updated cart item
     */
    CartItemResponseDto updateCartItem(Long id, CartItemRequestDto cartItemRequestDto);

    /**
     * Delete a cart item
     * @param id the ID of the cart item to delete
     */
    void deleteCartItem(Long id);

    /**
     * Delete all cart items for a specific cart
     * @param cartId the ID of the cart
     */
    void deleteCartItemsByCartId(Long cartId);

    /**
     * Delete all cart items for a specific cart
     * @param cart the cart
     */
    void deleteCartItemsByCart(Cart cart);
}
