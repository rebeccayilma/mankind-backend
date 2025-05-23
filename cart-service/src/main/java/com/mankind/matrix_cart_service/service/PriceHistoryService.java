package com.mankind.matrix_cart_service.service;

import com.mankind.matrix_cart_service.dto.PriceHistoryResponseDto;
import com.mankind.matrix_cart_service.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public interface PriceHistoryService {
    
    /**
     * Get all price history records for a specific cart item
     * @param cartItemId the ID of the cart item
     * @return list of price history records
     */
    List<PriceHistoryResponseDto> getPriceHistoryByCartItemId(Long cartItemId);
    
    /**
     * Record a price change for a cart item
     * @param cartItem the cart item
     * @param oldPrice the old price
     */
    void recordPriceChange(CartItem cartItem, BigDecimal oldPrice);
}