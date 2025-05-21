package com.mankind.matrix_product_service.mapper;

import com.mankind.matrix_product_service.dto.product.ProductDTO;
import com.mankind.matrix_product_service.dto.product.ProductResponseDTO;
import com.mankind.matrix_product_service.dto.inventory.InventoryStatusDTO;
import com.mankind.matrix_product_service.model.Inventory;
import com.mankind.matrix_product_service.model.Product;
import org.mapstruct.*;
import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    Product toEntity(ProductDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "model", ignore = true)
    @Mapping(target = "specifications", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateEntity(@MappingTarget Product product, ProductDTO dto);

    @Mapping(target = "inventoryStatus", expression = "java(mapInventoryStatus(product.getInventory()))")
    @Mapping(target = "images", expression = "java(filterNullImages(product.getImages()))")
    ProductResponseDTO toResponseDTO(Product product);

    default String[] filterNullImages(String[] images) {
        if (images == null) {
            return new String[0];
        }
        return java.util.Arrays.stream(images)
            .filter(image -> image != null && !image.trim().isEmpty())
            .toArray(String[]::new);
    }

    default InventoryStatusDTO mapInventoryStatus(Inventory inventory) {
        if (inventory == null) {
            return InventoryStatusDTO.builder()
                .availableQuantity(BigDecimal.ZERO)
                .reservedQuantity(BigDecimal.ZERO)
                .soldQuantity(BigDecimal.ZERO)
                .totalQuantity(BigDecimal.ZERO)
                .status("NO_INVENTORY")
                .build();
        }

        BigDecimal totalQuantity = inventory.getAvailableQuantity().add(inventory.getReservedQuantity());
        return InventoryStatusDTO.builder()
            .productId(inventory.getProduct().getId())
            .productName(inventory.getProduct().getName())
            .availableQuantity(inventory.getAvailableQuantity())
            .reservedQuantity(inventory.getReservedQuantity())
            .soldQuantity(inventory.getSoldQuantity())
            .totalQuantity(totalQuantity)
            .status(determineStatus(inventory))
            .build();
    }

    default String determineStatus(Inventory inventory) {
        if (inventory == null) {
            return "NO_INVENTORY";
        }
        
        BigDecimal total = inventory.getAvailableQuantity().add(inventory.getReservedQuantity());
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            return "OUT_OF_STOCK";
        }
        
        return "IN_STOCK";
    }
}
