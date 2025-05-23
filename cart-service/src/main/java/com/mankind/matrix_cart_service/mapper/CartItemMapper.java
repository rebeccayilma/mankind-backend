package com.mankind.matrix_cart_service.mapper;

import com.mankind.matrix_cart_service.dto.CartItemRequestDto;
import com.mankind.matrix_cart_service.dto.CartItemResponseDto;
import com.mankind.matrix_cart_service.model.Cart;
import com.mankind.matrix_cart_service.model.CartItem;
import com.mankind.matrix_cart_service.repository.CartRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CartItemMapper {

    @Autowired
    protected CartRepository cartRepository;

    /**
     * Convert CartItem entity to CartItemResponseDto
     */
    @Mapping(target = "cartId", source = "cart.id")
    public abstract CartItemResponseDto toDto(CartItem cartItem);

    /**
     * Convert list of CartItem entities to list of CartItemResponseDto
     */
    public abstract List<CartItemResponseDto> toDtoList(List<CartItem> cartItems);

    /**
     * Convert CartItemRequestDto to CartItem entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    public abstract CartItem toEntity(CartItemRequestDto cartItemRequestDto);

    /**
     * Update CartItem entity from CartItemRequestDto
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    public abstract void updateEntityFromDto(CartItemRequestDto cartItemRequestDto, @MappingTarget CartItem cartItem);

    /**
     * After mapping, set the cart and calculate the total price
     */
    @AfterMapping
    protected void afterMapping(CartItemRequestDto dto, @MappingTarget CartItem entity) {
        // Set the cart
        if (dto.getCartId() != null) {
            Cart cart = cartRepository.findById(dto.getCartId())
                    .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + dto.getCartId()));
            entity.setCart(cart);
        }

        // Calculate total price
        if (entity.getPriceAtAddition() != null && entity.getQuantity() != null) {
            entity.calculateTotalPrice();
        }
    }
}
