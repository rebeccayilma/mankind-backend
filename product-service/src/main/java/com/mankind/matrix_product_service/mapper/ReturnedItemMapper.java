package com.mankind.matrix_product_service.mapper;

import com.mankind.matrix_product_service.dto.returneditem.ReturnedItemDTO;
import com.mankind.matrix_product_service.dto.returneditem.ReturnedItemResponseDTO;
import com.mankind.matrix_product_service.model.ReturnedItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ProductMapper.class})
public interface ReturnedItemMapper {
    /**
     * Convert DTO to entity for creation
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    ReturnedItem toEntity(ReturnedItemDTO dto);

    /**
     * Update entity from DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateEntity(@MappingTarget ReturnedItem returnedItem, ReturnedItemDTO dto);

    /**
     * Convert entity to response DTO
     */
    @Mapping(target = "product", source = "product")
    ReturnedItemResponseDTO toResponseDTO(ReturnedItem returnedItem);
}