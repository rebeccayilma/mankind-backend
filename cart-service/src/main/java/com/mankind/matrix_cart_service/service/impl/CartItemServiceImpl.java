package com.mankind.matrix_cart_service.service.impl;

import com.mankind.matrix_cart_service.dto.CartItemRequestDto;
import com.mankind.matrix_cart_service.dto.CartItemResponseDto;
import com.mankind.matrix_cart_service.exception.ResourceNotFoundException;
import com.mankind.matrix_cart_service.mapper.CartItemMapper;
import com.mankind.matrix_cart_service.model.Cart;
import com.mankind.matrix_cart_service.model.CartItem;
import com.mankind.matrix_cart_service.repository.CartItemRepository;
import com.mankind.matrix_cart_service.repository.CartRepository;
import com.mankind.matrix_cart_service.service.CartItemService;
import com.mankind.matrix_cart_service.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartItemMapper cartItemMapper;
    private final PriceHistoryService priceHistoryService;

    @Override
    public List<CartItemResponseDto> getAllCartItems() {
        List<CartItem> cartItems = cartItemRepository.findAll();
        return cartItemMapper.toDtoList(cartItems);
    }

    @Override
    public CartItemResponseDto getCartItemById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", id));
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public List<CartItemResponseDto> getCartItemsByCartId(Long cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        return cartItemMapper.toDtoList(cartItems);
    }

    @Override
    public List<CartItemResponseDto> getCartItemsByCart(Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        return cartItemMapper.toDtoList(cartItems);
    }

    @Override
    @Transactional
    public CartItemResponseDto createCartItem(CartItemRequestDto cartItemRequestDto) {
        // Check if cart exists
        Cart cart = cartRepository.findById(cartItemRequestDto.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartItemRequestDto.getCartId()));

        // Check if cart item already exists for this cart and product
        CartItem existingCartItem = cartItemRepository.findByCartIdAndProductId(
                cartItemRequestDto.getCartId(), cartItemRequestDto.getProductId());

        if (existingCartItem != null) {
            // Update quantity instead of creating a new item
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequestDto.getQuantity());
            CartItem savedCartItem = cartItemRepository.save(existingCartItem);
            return cartItemMapper.toDto(savedCartItem);
        }

        // Create new cart item
        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDto);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    @Transactional
    public CartItemResponseDto updateCartItem(Long id, CartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", id));

        // Store the old price before updating
        BigDecimal oldPrice = cartItem.getPriceAtAddition();

        cartItemMapper.updateEntityFromDto(cartItemRequestDto, cartItem);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        // Record price change if the price has changed
        priceHistoryService.recordPriceChange(updatedCartItem, oldPrice);

        return cartItemMapper.toDto(updatedCartItem);
    }

    @Override
    @Transactional
    public void deleteCartItem(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("CartItem", "id", id);
        }
        cartItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCartItemsByCartId(Long cartId) {
        cartItemRepository.deleteByCartId(cartId);
    }

    @Override
    @Transactional
    public void deleteCartItemsByCart(Cart cart) {
        cartItemRepository.deleteByCart(cart);
    }
}
