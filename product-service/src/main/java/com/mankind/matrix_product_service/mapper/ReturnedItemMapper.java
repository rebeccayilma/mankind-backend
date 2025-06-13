package com.mankind.matrix_product_service.mapper;

import com.mankind.matrix_product_service.dto.returneditem.ReturnedItemDTO;
import com.mankind.matrix_product_service.dto.returneditem.ReturnedItemResponseDTO;
import com.mankind.matrix_product_service.model.ReturnedItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReturnedItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ReturnedItem toEntity(ReturnedItemDTO dto);

    ReturnedItemResponseDTO toResponseDTO(ReturnedItem returnedItem);

    List<ReturnedItemResponseDTO> toResponseDTOList(List<ReturnedItem> returnedItems);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget ReturnedItem returnedItem, ReturnedItemDTO dto);
}