package com.mankind.matrix_order_service.controller;

import com.mankind.matrix_order_service.dto.OrderResponseDTO;
import com.mankind.matrix_order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Order API", description = "Admin endpoints for managing orders. All endpoints require ADMIN role.")
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "Get all orders (admin)", description = "Retrieves a paginated list of all orders in the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required")
    })
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @Parameter(description = "Pagination and sorting parameters") Pageable pageable) {
        // TODO: Implement admin service method for getting all orders
        return ResponseEntity.ok(Page.empty(pageable));
    }

    @Operation(summary = "Get order by ID (admin)", description = "Retrieves a specific order by its ID. Admin can access any order.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @Parameter(description = "ID of the order to retrieve", required = true) 
            @PathVariable Long orderId) {
        // TODO: Implement admin service method for getting order by ID
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update order status (admin)", description = "Updates the status of an order.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status transition"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @Parameter(description = "ID of the order to update", required = true) 
            @PathVariable Long orderId,
            @Parameter(description = "New status", required = true) 
            @RequestParam String status) {
        // TODO: Implement admin service method for updating order status
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cancel order (admin)", description = "Cancels an order if it's in a cancellable status (not shipped or delivered). Only admins can cancel orders.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
        @ApiResponse(responseCode = "400", description = "Order cannot be cancelled in current status"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(
            @Parameter(description = "ID of the order to cancel", required = true) 
            @PathVariable Long orderId) {
        OrderResponseDTO order = orderService.cancelOrderByAdmin(orderId);
        return ResponseEntity.ok(order);
    }
}
