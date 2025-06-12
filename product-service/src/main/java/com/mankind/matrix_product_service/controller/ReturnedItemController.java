package com.mankind.matrix_product_service.controller;

import com.mankind.matrix_product_service.dto.returneditem.ReturnedItemDTO;
import com.mankind.matrix_product_service.dto.returneditem.ReturnedItemResponseDTO;
import com.mankind.matrix_product_service.service.ReturnedItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/returns")
@RequiredArgsConstructor
@Tag(name = "Returned Items Management", description = "APIs for managing returned items")
public class ReturnedItemController {
    private final ReturnedItemService returnedItemService;

    @Operation(summary = "Create a new returned item", description = "Creates a new returned item in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Returned item successfully created",
                    content = @Content(schema = @Schema(implementation = ReturnedItemResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ReturnedItemResponseDTO> createReturnedItem(
            @Parameter(description = "Returned item object to be created", required = true)
            @Valid @RequestBody ReturnedItemDTO returnedItemDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(returnedItemService.createReturnedItem(returnedItemDTO));
    }

    @Operation(summary = "Get all returned items", description = "Retrieves a paginated list of all returned items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved returned items",
                    content = @Content(schema = @Schema(implementation = ReturnedItemResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<ReturnedItemResponseDTO>> getAllReturnedItems(
            @Parameter(description = "Pagination and sorting parameters")
            Pageable pageable) {
        return ResponseEntity.ok(returnedItemService.getAllReturnedItems(pageable));
    }

    @Operation(summary = "Get returned item by ID", description = "Retrieves a specific returned item by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved returned item",
                    content = @Content(schema = @Schema(implementation = ReturnedItemResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Returned item not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReturnedItemResponseDTO> getReturnedItemById(
            @Parameter(description = "ID of the returned item to retrieve", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(returnedItemService.getReturnedItemById(id));
    }

    @Operation(summary = "Get returned items by user", description = "Retrieves a paginated list of returned items for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved returned items",
                    content = @Content(schema = @Schema(implementation = ReturnedItemResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReturnedItemResponseDTO>> getReturnedItemsByUserId(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Pagination and sorting parameters")
            Pageable pageable) {
        return ResponseEntity.ok(returnedItemService.getReturnedItemsByUserId(userId, pageable));
    }

    @Operation(summary = "Get returned items by status", description = "Retrieves a paginated list of returned items with a specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved returned items",
                    content = @Content(schema = @Schema(implementation = ReturnedItemResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ReturnedItemResponseDTO>> getReturnedItemsByStatus(
            @Parameter(description = "Status of the returned items", required = true)
            @PathVariable String status,
            @Parameter(description = "Pagination and sorting parameters")
            Pageable pageable) {
        return ResponseEntity.ok(returnedItemService.getReturnedItemsByStatus(status, pageable));
    }

    @Operation(summary = "Update returned item", description = "Updates an existing returned item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returned item successfully updated",
                    content = @Content(schema = @Schema(implementation = ReturnedItemResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Returned item or product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReturnedItemResponseDTO> updateReturnedItem(
            @Parameter(description = "ID of the returned item to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated returned item object", required = true)
            @Valid @RequestBody ReturnedItemDTO returnedItemDTO) {
        return ResponseEntity.ok(returnedItemService.updateReturnedItem(id, returnedItemDTO));
    }

    @Operation(summary = "Delete returned item", description = "Deletes a returned item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Returned item successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Returned item not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturnedItem(
            @Parameter(description = "ID of the returned item to delete", required = true)
            @PathVariable Long id) {
        returnedItemService.deleteReturnedItem(id);
        return ResponseEntity.noContent().build();
    }
}