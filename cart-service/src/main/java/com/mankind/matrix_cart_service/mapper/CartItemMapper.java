package com.mankind.matrix_cart_service.mapper;

import com.mankind.matrix_cart_service.dto.CartItemDTO;
import com.mankind.matrix_cart_service.dto.CartItemResponseDTO;
import com.mankind.matrix_cart_service.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    CartItem toEntity(CartItemDTO dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    CartItemResponseDTO toResponseDTO(CartItem entity);

    void updateEntityFromDTO(CartItemDTO dto, @MappingTarget CartItem entity);
} 