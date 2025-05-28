package com.mankind.matrix_cart_service.service.impl;

import com.mankind.matrix_cart_service.dto.CartRequestDto;
import com.mankind.matrix_cart_service.dto.CartResponseDto;
import com.mankind.matrix_cart_service.exception.ResourceNotFoundException;
import com.mankind.matrix_cart_service.mapper.CartMapper;
import com.mankind.matrix_cart_service.model.Cart;
import com.mankind.matrix_cart_service.model.CartStatus;
import com.mankind.matrix_cart_service.repository.CartRepository;
import com.mankind.matrix_cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    public List<CartResponseDto> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return cartMapper.toDtoList(carts);
    }

    @Override
    public CartResponseDto getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));
        return cartMapper.toDto(cart);
    }

    @Override
    public CartResponseDto getActiveCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for user with ID: " + userId));
        return cartMapper.toDto(cart);
    }

    @Override
    public CartResponseDto getActiveCartBySessionId(String sessionId) {
        Cart cart = cartRepository.findBySessionIdAndStatus(sessionId, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for session: " + sessionId));
        return cartMapper.toDto(cart);
    }

    @Override
    public List<CartResponseDto> getCartsByUserId(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        return cartMapper.toDtoList(carts);
    }

    @Override
    public List<CartResponseDto> getCartsBySessionId(String sessionId) {
        List<Cart> carts = cartRepository.findBySessionId(sessionId);
        return cartMapper.toDtoList(carts);
    }

    @Override
    public List<CartResponseDto> getCartsByStatus(CartStatus status) {
        List<Cart> carts = cartRepository.findByStatus(status);
        return cartMapper.toDtoList(carts);
    }

    @Override
    @Transactional
    public CartResponseDto createCart(CartRequestDto cartRequestDto) {
        Cart cart = cartMapper.toEntity(cartRequestDto);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDto(savedCart);
    }

    @Override
    @Transactional
    public CartResponseDto updateCart(Long id, CartRequestDto cartRequestDto) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));
        
        cartMapper.updateEntityFromDto(cartRequestDto, cart);
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toDto(updatedCart);
    }

    @Override
    @Transactional
    public CartResponseDto updateCartStatus(Long id, CartStatus status) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));
        
        cart.setStatus(status);
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toDto(updatedCart);
    }

    @Override
    @Transactional
    public void deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cart", "id", id);
        }
        cartRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCartsByUserId(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(carts);
    }

    @Override
    @Transactional
    public void deleteCartsBySessionId(String sessionId) {
        List<Cart> carts = cartRepository.findBySessionId(sessionId);
        cartRepository.deleteAll(carts);
    }
}