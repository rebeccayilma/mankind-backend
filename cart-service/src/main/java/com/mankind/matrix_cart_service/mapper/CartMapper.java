package com.mankind.matrix_cart_service.mapper;

import com.mankind.matrix_cart_service.dto.CartRequestDto;
import com.mankind.matrix_cart_service.dto.CartResponseDto;
import com.mankind.matrix_cart_service.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {

    /**
     * Convert Cart entity to CartResponseDto
     */
    @Mapping(target = "totalItems", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    CartResponseDto toDto(Cart cart);

    /**
     * Convert list of Cart entities to list of CartResponseDto
     */
    List<CartResponseDto> toDtoList(List<Cart> carts);

    /**
     * Convert CartRequestDto to Cart entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    Cart toEntity(CartRequestDto cartRequestDto);

    /**
     * Update Cart entity from CartRequestDto
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    void updateEntityFromDto(CartRequestDto cartRequestDto, @MappingTarget Cart cart);

    /**
     * After mapping, calculate the totals
     */
    @AfterMapping
    default void calculateTotals(Cart cart, @MappingTarget CartResponseDto dto) {
        dto.calculateTotals();
    }
}