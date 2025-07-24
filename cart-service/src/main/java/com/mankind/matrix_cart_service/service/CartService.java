package com.mankind.matrix_cart_service.service;

import com.mankind.api.product.dto.product.ProductResponseDTO;
import com.mankind.matrix_cart_service.client.ProductClient;
import com.mankind.matrix_cart_service.dto.CartItemDTO;
import com.mankind.matrix_cart_service.dto.CartResponseDTO;
import com.mankind.matrix_cart_service.mapper.CartItemMapper;
import com.mankind.matrix_cart_service.mapper.CartMapper;
import com.mankind.matrix_cart_service.model.Cart;
import com.mankind.matrix_cart_service.model.CartItem;
import com.mankind.matrix_cart_service.model.CartStatus;
import com.mankind.matrix_cart_service.repository.CartItemRepository;
import com.mankind.matrix_cart_service.repository.CartRepository;
import com.mankind.matrix_cart_service.exception.MaxQuantityPerPurchaseExceededException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import feign.FeignException;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CartService {
    private static final Logger log = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final CurrentUserService currentUserService;
    private final ProductClient productClient;

    public CartResponseDTO getCurrentUserOpenCart() {
        Long userId = currentUserService.getCurrentUserId();
        Optional<Cart> cartOpt = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE);
        return cartOpt.map(cartMapper::toResponseDTO).orElse(null);
    }

    @Transactional
    public CartResponseDTO addItemToCart(CartItemDTO itemDTO) {
        log.info("addItemToCart called with: {}", itemDTO);
        Long userId = currentUserService.getCurrentUserId();
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .status(CartStatus.ACTIVE)
                            .build();
                    return cartRepository.save(newCart);
                });
        try {
            // Fetch inventory info for price and maxQuantityPerPurchase
            ResponseEntity<com.mankind.api.product.dto.inventory.InventoryResponseDTO> inventoryResponse = productClient.getInventoryByProductId(itemDTO.getProductId());
            if (!inventoryResponse.getStatusCode().is2xxSuccessful() || inventoryResponse.getBody() == null) {
                throw new EntityNotFoundException("Product inventory not found");
            }
            var inventory = inventoryResponse.getBody();
            if (inventory.getMaxQuantityPerPurchase() != null && itemDTO.getQuantity() > inventory.getMaxQuantityPerPurchase().intValue()) {
                throw new MaxQuantityPerPurchaseExceededException(
                    "Quantity " + itemDTO.getQuantity() + " exceeds the maximum allowed per purchase (" + inventory.getMaxQuantityPerPurchase().intValue() + ") for this product.");
            }
            // Check if item exists
            CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), itemDTO.getProductId());
            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + itemDTO.getQuantity());
            } else {
                double price = inventory.getPrice().doubleValue();
                cartItem = CartItem.builder()
                        .cart(cart)
                        .productId(itemDTO.getProductId())
                        .quantity(itemDTO.getQuantity())
                        .price(price)
                        .build();
                cart.getCartItems().add(cartItem);
            }
            cartItemRepository.save(cartItem);
            cartRepository.save(cart);
            return cartMapper.toResponseDTO(cart);
        } catch (FeignException e) {
            log.error("Feign error when calling product-service: status={}, content={}", e.status(), e.contentUTF8());
            throw e;
        } catch (ResponseStatusException e) {
            log.error("ResponseStatusException: status={}, reason={}", e.getStatusCode(), e.getReason());
            throw e;
        }
    }

    @Transactional
    public CartResponseDTO updateItemQuantity(Long productId, int quantity) {
        Long userId = currentUserService.getCurrentUserId();
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Active cart not found"));
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        if (cartItem == null) {
            throw new EntityNotFoundException("Cart item not found");
        }
        try {
            // Fetch inventory info for maxQuantityPerPurchase
            ResponseEntity<com.mankind.api.product.dto.inventory.InventoryResponseDTO> inventoryResponse = productClient.getInventoryByProductId(productId);
            if (!inventoryResponse.getStatusCode().is2xxSuccessful() || inventoryResponse.getBody() == null) {
                throw new EntityNotFoundException("Product inventory not found");
            }
            var inventory = inventoryResponse.getBody();
            if (inventory.getMaxQuantityPerPurchase() != null && quantity > inventory.getMaxQuantityPerPurchase().intValue()) {
                throw new MaxQuantityPerPurchaseExceededException(
                    "Quantity " + quantity + " exceeds the maximum allowed per purchase (" + inventory.getMaxQuantityPerPurchase().intValue() + ") for this product.");
            }
            if (quantity <= 0) {
                cart.getCartItems().remove(cartItem);
                cartItemRepository.delete(cartItem);
            } else {
                cartItem.setQuantity(quantity);
                cartItemRepository.save(cartItem);
            }
            // If cart is empty, close it
            if (cart.getCartItems().isEmpty()) {
                cart.setStatus(CartStatus.REMOVED); // Use REMOVED as the closed status
            }
            cartRepository.save(cart);
            return cartMapper.toResponseDTO(cart);
        } catch (FeignException e) {
            log.error("Feign error when calling product-service: status={}, content={}", e.status(), e.contentUTF8());
            throw e;
        } catch (ResponseStatusException e) {
            log.error("ResponseStatusException: status={}, reason={}", e.getStatusCode(), e.getReason());
            throw e;
        }
    }

    @Transactional
    public CartResponseDTO removeItemFromCart(Long productId) {
        return updateItemQuantity(productId, 0);
    }
}
