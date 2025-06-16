package com.mankind.matrix_product_service.mapper;

import com.mankind.api.product.dto.inventory.InventoryDTO;
import com.mankind.api.product.dto.inventory.InventoryResponseDTO;
import com.mankind.api.product.dto.inventory.InventoryStatusDTO;
import com.mankind.matrix_product_service.model.Inventory;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reservedQuantity", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "soldQuantity", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "maxQuantityPerPurchase", source = "maxQuantityPerPurchase")
    Inventory toEntity(InventoryDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reservedQuantity", ignore = true)
    @Mapping(target = "soldQuantity", ignore = true)
    @Mapping(target = "maxQuantityPerPurchase", source = "maxQuantityPerPurchase")
    void updateEntity(@MappingTarget Inventory entity, InventoryDTO dto);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "priceDisplay", expression = "java(formatPriceDisplay(inventory.getPrice(), inventory.getCurrency()))")
    @Mapping(target = "maxQuantityPerPurchase", source = "maxQuantityPerPurchase")
    @Mapping(target = "status", expression = "java(determineStatus(inventory))")
    InventoryResponseDTO toResponseDTO(Inventory inventory);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "totalQuantity", expression = "java(inventory.getAvailableQuantity().add(inventory.getReservedQuantity()))")
    @Mapping(target = "status", expression = "java(determineStatus(inventory))")
    InventoryStatusDTO toStatusDTO(Inventory inventory);

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

    default String formatPriceDisplay(BigDecimal price, String currency) {
        if (price == null || currency == null) {
            return null;
        }
        return String.format("%s %.2f", currency, price);
    }
} 