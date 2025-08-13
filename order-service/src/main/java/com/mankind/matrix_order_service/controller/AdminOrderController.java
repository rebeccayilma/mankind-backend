package com.mankind.matrix_order_service.controller;

import com.mankind.matrix_order_service.dto.OrderResponseDTO;
import com.mankind.matrix_order_service.dto.AdminOrderFilters;
import com.mankind.matrix_order_service.dto.UpdateOrderStatusRequest;
import com.mankind.matrix_order_service.service.OrderService;
import com.mankind.matrix_order_service.model.Order;
import com.mankind.matrix_order_service.service.RoleVerificationService;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Order API", description = "Admin endpoints for managing orders. All endpoints require ADMIN role. Access through the gateway at /api/v1/admin/orders/*")
public class AdminOrderController {

    private final OrderService orderService;
    private final RoleVerificationService roleVerificationService;

    @Operation(summary = "Get all orders (admin)", description = "Retrieves a paginated list of all orders in the system with optional filters. Supports filtering by status, payment status, order number, and date range. Pagination is required, filters are optional.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required")
    })
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @Parameter(description = "Order status filter (optional)")
            @RequestParam(required = false) String status,
            
            @Parameter(description = "Payment status filter (optional)")
            @RequestParam(required = false) String paymentStatus,
            
            @Parameter(description = "Order number filter - partial match (optional)")
            @RequestParam(required = false) String orderNumber,
            
            @Parameter(description = "Start date for created date range filter - ISO format (optional)")
            @RequestParam(required = false) String createdAtFrom,
            
            @Parameter(description = "End date for created date range filter - ISO format (optional)")
            @RequestParam(required = false) String createdAtTo,
            
            @Parameter(description = "Page number (0-based, default: 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size (default: 20)")
            @RequestParam(defaultValue = "20") int size) {
        
        // Verify admin role
        roleVerificationService.verifyAdminOrSuperAdminRole();
        
        // Build filters object
        AdminOrderFilters filters = buildFiltersFromRequestParams(status, paymentStatus, orderNumber, createdAtFrom, createdAtTo);
        
        // Create pageable with safe sorting
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, 
            org.springframework.data.domain.Sort.by("createdAt").descending());
        
        Page<OrderResponseDTO> orders = orderService.getAllOrdersWithFilters(filters, pageable);
        return ResponseEntity.ok(orders);
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
        
        // Verify admin role
        roleVerificationService.verifyAdminOrSuperAdminRole();
        
        OrderResponseDTO order = orderService.getOrderByIdForAdmin(orderId);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Update order status (admin)", description = "Updates the status of an order with validation of status transitions.")
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
            @Parameter(description = "Status update request", required = true)
            @RequestBody UpdateOrderStatusRequest request) {
        
        // Verify admin role
        roleVerificationService.verifyAdminOrSuperAdminRole();
        
        OrderResponseDTO order = orderService.updateOrderStatusByAdmin(orderId, request.getStatus(), request.getNotes());
        return ResponseEntity.ok(order);
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
        
        // Verify admin role
        roleVerificationService.verifyAdminOrSuperAdminRole();
        
        OrderResponseDTO order = orderService.cancelOrderByAdmin(orderId);
        return ResponseEntity.ok(order);
    }

    private AdminOrderFilters buildFiltersFromRequestParams(String status, String paymentStatus, String orderNumber, String createdAtFrom, String createdAtTo) {
        AdminOrderFilters filters = new AdminOrderFilters();
        
        if (status != null && !status.isEmpty()) {
            try {
                filters.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid order status: {}", status);
            }
        }
        
        if (paymentStatus != null && !paymentStatus.isEmpty()) {
            try {
                filters.setPaymentStatus(Order.PaymentStatus.valueOf(paymentStatus.toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid payment status: {}", paymentStatus);
            }
        }
        
        if (orderNumber != null && !orderNumber.isEmpty()) {
            filters.setOrderNumber(orderNumber);
        }
        
        if (createdAtFrom != null && !createdAtFrom.isEmpty()) {
            try {
                filters.setCreatedAtFrom(LocalDateTime.parse(createdAtFrom, DateTimeFormatter.ISO_DATE_TIME));
            } catch (DateTimeParseException e) {
                log.warn("Invalid createdAtFrom date format: {}", createdAtFrom);
            }
        }
        
        if (createdAtTo != null && !createdAtTo.isEmpty()) {
            try {
                filters.setCreatedAtTo(LocalDateTime.parse(createdAtTo, DateTimeFormatter.ISO_DATE_TIME));
            } catch (DateTimeParseException e) {
                log.warn("Invalid createdAtTo date format: {}", createdAtTo);
            }
        }
        
        return filters;
    }
}
