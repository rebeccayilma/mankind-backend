package com.mankind.matrix_cart_service.mapper;

import com.mankind.matrix_cart_service.dto.PriceHistoryResponseDto;
import com.mankind.matrix_cart_service.model.PriceHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PriceHistoryMapper {

    /**
     * Convert PriceHistory entity to PriceHistoryResponseDto
     */
    PriceHistoryResponseDto toDto(PriceHistory priceHistory);

    /**
     * Convert list of PriceHistory entities to list of PriceHistoryResponseDto
     */
    List<PriceHistoryResponseDto> toDtoList(List<PriceHistory> priceHistories);
}