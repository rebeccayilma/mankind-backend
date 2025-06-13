package com.mankind.matrix_product_service.controller;

import com.mankind.matrix_product_service.dto.inventory.InventoryDTO;
import com.mankind.matrix_product_service.dto.inventory.InventoryLogDTO;
import com.mankind.matrix_product_service.dto.inventory.InventoryResponseDTO;
import com.mankind.matrix_product_service.dto.inventory.InventoryStatusDTO;
import com.mankind.matrix_product_service.dto.inventory.OutOfStockRequestDTO;
import com.mankind.matrix_product_service.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory management APIs")
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/{productId}")
    @Operation(summary = "Create inventory", description = "Creates inventory information for a product with initial stock and price")
    public ResponseEntity<InventoryResponseDTO> createInventory(
            @PathVariable Long productId,
            @Valid @RequestBody InventoryDTO inventoryDTO) {
        return ResponseEntity.ok(inventoryService.createInventory(productId, inventoryDTO));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get inventory", description = "Retrieves inventory information for a product")
    public ResponseEntity<InventoryResponseDTO> getInventory(
            @PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @GetMapping("/{productId}/status")
    @Operation(summary = "Get inventory status", description = "Retrieves simplified inventory status for a product")
    public ResponseEntity<InventoryStatusDTO> getInventoryStatus(
            @PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryStatus(productId));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update inventory", description = "Updates inventory stock and/or price information")
    public ResponseEntity<InventoryResponseDTO> updateInventory(
            @PathVariable Long productId,
            @Valid @RequestBody InventoryDTO inventoryDTO) {
        return ResponseEntity.ok(inventoryService.updateInventory(productId, inventoryDTO));
    }

    @GetMapping("/{productId}/logs")
    @Operation(summary = "Get inventory logs", description = "Retrieves inventory movement logs for a product")
    public ResponseEntity<List<InventoryLogDTO>> getInventoryLogs(
            @PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryLogs(productId));
    }

    @PatchMapping("/{productId}/out-of-stock")
    @Operation(summary = "Mark product as out of stock", 
               description = "Marks a product as out of stock by setting available quantity to zero and updating sold quantity")
    public ResponseEntity<InventoryResponseDTO> markProductOutOfStock(
            @PathVariable Long productId,
            @RequestBody(required = false) OutOfStockRequestDTO requestDTO) {
        String reason = requestDTO != null ? requestDTO.getReason() : null;
        return ResponseEntity.ok(inventoryService.markProductOutOfStock(productId, reason));
    }
} 
