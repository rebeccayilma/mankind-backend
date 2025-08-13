package com.mankind.matrix_order_service.service;

import com.mankind.api.cart.dto.CartResponseDTO;
import com.mankind.api.cart.dto.CartItemResponseDTO;
import com.mankind.api.coupon.dto.CouponDTO;
import com.mankind.api.user.dto.AddressDTO;
import com.mankind.matrix_order_service.client.CartClient;
import com.mankind.matrix_order_service.client.CouponClient;
import com.mankind.matrix_order_service.client.UserClient;
import com.mankind.matrix_order_service.dto.CreateOrderRequest;
import com.mankind.matrix_order_service.dto.OrderResponseDTO;
import com.mankind.matrix_order_service.dto.OrderItemDTO;
import com.mankind.matrix_order_service.dto.OrderCouponDTO;
import com.mankind.matrix_order_service.dto.AdminOrderFilters;
import com.mankind.matrix_order_service.dto.UpdateOrderStatusRequest;
import com.mankind.matrix_order_service.exception.CartValidationException;
import com.mankind.matrix_order_service.exception.CouponValidationException;
import com.mankind.matrix_order_service.exception.OrderCreationException;
import com.mankind.matrix_order_service.exception.OrderNotFoundException;
import com.mankind.matrix_order_service.exception.AccessDeniedException;
import com.mankind.matrix_order_service.model.Order;
import com.mankind.matrix_order_service.model.OrderItem;
import com.mankind.matrix_order_service.model.OrderStatusHistory;
import com.mankind.matrix_order_service.repository.OrderRepository;
import com.mankind.matrix_order_service.repository.OrderItemRepository;
import com.mankind.matrix_order_service.repository.OrderStatusHistoryRepository;
import com.mankind.matrix_order_service.repository.OrderPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final CartClient cartClient;
    private final CouponClient couponClient;

    private final UserClient userClient;
    private final CurrentUserService currentUserService;
    private final OrderNumberGenerator orderNumberGenerator;

    @Transactional
    public OrderResponseDTO createOrder(CreateOrderRequest request) {
        log.info("Creating order for current user");

        // Get current user
        Long userId = currentUserService.getCurrentUserId();

        // Validate cart and addresses
        CartResponseDTO cart = validateAndGetCart(userId);
        validateAddresses(request, userId);
        validateShippingValue(request);

        // Generate order number and calculate totals
        String orderNumber = orderNumberGenerator.generateOrderNumber();
        // Round shipping value to 2 decimal places
        BigDecimal roundedShippingValue = request.getShippingValue().setScale(2, RoundingMode.HALF_UP);
        OrderCalculation calculation = calculateOrderTotals(cart, request.getCouponCode(), roundedShippingValue);

        // Create and save order
        Order order = createOrderEntity(request, userId, cart, orderNumber, calculation, roundedShippingValue);
        final Order savedOrder = orderRepository.save(order);

        // Process order items
        List<OrderItem> orderItems = createOrderItems(cart, savedOrder);

        // Update cart status and create status history
        updateCartStatus(cart.getId(), savedOrder.getId());
        createOrderStatusHistory(savedOrder, "Order created from cart");

        // Mark coupon as used if applied
        if (calculation.getAppliedCoupon() != null) {
            markCouponAsUsed(request.getCouponCode(), savedOrder.getId());
        }

        log.info("Order created successfully: {} with {} items", orderNumber, orderItems.size());

        return buildOrderResponseDTO(savedOrder, orderItems, calculation);
    }

    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = findOrderById(orderId);
        validateOrderOwnership(order);
        return buildOrderResponseDTO(order, null, null);
    }

    public OrderResponseDTO getOrderByIdForAdmin(Long orderId) {
        Order order = findOrderById(orderId);
        // Admin can access any order, no ownership validation needed
        return buildOrderResponseDTO(order, null, null);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatusByAdmin(Long orderId, Order.OrderStatus newStatus, String notes) {
        Order order = findOrderById(orderId);
        validateOrderStatusTransition(order.getStatus(), newStatus);
        
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        
        String statusChangeNote = notes != null ? notes : String.format("Order status changed to %s by admin", newStatus);
        createOrderStatusHistory(order, statusChangeNote);
        
        log.info("Order {} status updated to {} by admin", orderId, newStatus);
        return buildOrderResponseDTO(order, null, null);
    }

    public Page<OrderResponseDTO> getUserOrdersPaginated(Pageable pageable) {
        Long userId = currentUserService.getCurrentUserId();
        Page<Order> ordersPage = orderRepository.findByUserIdOrderByCreatedAtDesc(userId.toString(), pageable);
        return ordersPage.map(order -> buildOrderResponseDTO(order, null, null));
    }

    public Page<OrderResponseDTO> getAllOrdersWithFilters(AdminOrderFilters filters, Pageable pageable) {
        log.info("Admin querying all orders with filters: {}", filters);
        
        // Create a new Pageable with safe default sorting
        Pageable safePageable = org.springframework.data.domain.PageRequest.of(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            org.springframework.data.domain.Sort.by("createdAt").descending()
        );
        
        Page<Order> ordersPage = orderRepository.findAllWithFilters(
            filters.getStatus(),
            filters.getPaymentStatus(),
            filters.getOrderNumber(),
            filters.getCreatedAtFrom(),
            filters.getCreatedAtTo(),
            safePageable
        );
        
        return ordersPage.map(order -> buildOrderResponseDTO(order, null, null));
    }

    @Transactional
    public OrderResponseDTO cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        validateOrderOwnership(order);
        validateOrderCanBeCancelled(order);

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);

        createOrderStatusHistory(order, "Order cancelled by user");
        return buildOrderResponseDTO(order, null, null);
    }

    @Transactional
    public OrderResponseDTO cancelOrderByAdmin(Long orderId) {
        Order order = findOrderById(orderId);
        validateOrderCanBeCancelled(order);

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);

        createOrderStatusHistory(order, "Order cancelled by admin");
        return buildOrderResponseDTO(order, null, null);
    }

    // Private helper methods

    private CartResponseDTO validateAndGetCart(Long userId) {
        try {
            CartResponseDTO cart = cartClient.getCurrentUserCart();
            if (cart == null) {
                throw new CartValidationException("No active cart found for current user. Please add items to your cart before creating an order.");
            }

            if (!"ACTIVE".equals(cart.getStatus())) {
                throw new CartValidationException("Cart is not active. Current status: " + cart.getStatus());
            }

            return cart;
        } catch (Exception e) {
            log.error("Failed to get current user cart: {}", e.getMessage());
            
            // Provide more specific error messages
            if (e.getMessage().contains("404") || e.getMessage().contains("Cart not found")) {
                throw new CartValidationException("No active cart found. Please add items to your cart before creating an order.");
            } else if (e.getMessage().contains("Connection refused")) {
                throw new CartValidationException("Cannot connect to cart service. Service may not be running.");
            } else {
                throw new CartValidationException("Failed to retrieve cart: " + e.getMessage());
            }
        }
    }

    private void validateAddresses(CreateOrderRequest request, Long userId) {
        validateAddressOwnership(request.getShippingAddressId(), "shipping", userId);
    }

    private void validateShippingValue(CreateOrderRequest request) {
        if (request.getShippingValue() == null) {
            throw new CartValidationException("Shipping value is required");
        }
        if (request.getShippingValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new CartValidationException("Shipping value must be greater than or equal to 0");
        }
        // Additional validation: shipping value should not be excessively high (e.g., > 1000)
        if (request.getShippingValue().compareTo(BigDecimal.valueOf(1000)) > 0) {
            throw new CartValidationException("Shipping value seems excessively high. Please verify the amount.");
        }
        log.info("Validated shipping value: {}", request.getShippingValue());
    }

    private void validateAddressOwnership(Long addressId, String addressType, Long userId) {
        try {
            AddressDTO address = userClient.getAddressById(addressId);
            if (!address.getUserId().equals(userId)) {
                throw new CartValidationException(String.format("%s address does not belong to current user", addressType));
            }
            log.info("Validated {} address: {}", addressType, addressId);
        } catch (Exception e) {
            log.error("Failed to validate {} address {}: {}", addressType, addressId, e.getMessage());
            throw new CartValidationException(String.format("Invalid %s address: %s", addressType, addressId));
        }
    }

    private OrderCalculation calculateOrderTotals(CartResponseDTO cart, String couponCode, BigDecimal shippingValue) {
        // Calculate subtotal from cart items and round to 2 decimal places
        BigDecimal subtotal = cart.getItems().stream()
                .mapToDouble(item -> item.getSubtotal())
                .mapToObj(amount -> BigDecimal.valueOf(amount))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        
        // Calculate discount from coupon if provided
        BigDecimal discounts = BigDecimal.ZERO;
        CouponDTO appliedCoupon = null;

        if (couponCode != null && !couponCode.isEmpty()) {
            appliedCoupon = validateAndApplyCoupon(couponCode, subtotal);
            discounts = calculateDiscount(appliedCoupon, subtotal);
        }

        // Calculate tax as 10% of (subtotal - discounts)
        BigDecimal taxableAmount = subtotal.subtract(discounts).setScale(2, RoundingMode.HALF_UP);
        BigDecimal tax = taxableAmount.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);

        // Calculate total: subtotal - discounts + tax + shipping
        BigDecimal total = taxableAmount.add(tax).add(shippingValue).setScale(2, RoundingMode.HALF_UP);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        return new OrderCalculation(subtotal, tax, discounts, shippingValue, total, appliedCoupon);
    }

    private CouponDTO validateAndApplyCoupon(String couponCode, BigDecimal totalAmount) {
        try {
            CouponDTO coupon = couponClient.validateCoupon(couponCode);
            if (coupon != null) {
                BigDecimal discountAmount = calculateDiscount(coupon, totalAmount);
                log.info("Applied coupon {}: discount amount {}", couponCode, discountAmount);
                return coupon;
            }
        } catch (Exception e) {
            log.warn("Failed to validate coupon {}: {}", couponCode, e.getMessage());
            throw new CouponValidationException("Invalid coupon code: " + couponCode);
        }
        throw new CouponValidationException("Invalid coupon code: " + couponCode);
    }

    private BigDecimal calculateDiscount(CouponDTO coupon, BigDecimal totalAmount) {
        BigDecimal discount;
        if (coupon.getType() == CouponDTO.CouponType.PERCENTAGE) {
            discount = totalAmount.multiply(coupon.getValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (coupon.getType() == CouponDTO.CouponType.FIXED_AMOUNT) {
            discount = coupon.getValue();
        } else {
            discount = BigDecimal.ZERO;
        }
        return discount.setScale(2, RoundingMode.HALF_UP);
    }

    private Order createOrderEntity(CreateOrderRequest request, Long userId, CartResponseDTO cart, 
                                  String orderNumber, OrderCalculation calculation, BigDecimal roundedShippingValue) {
        return Order.builder()
                .orderNumber(orderNumber)
                .userId(userId.toString())
                .cartId(cart.getId())
                .status(Order.OrderStatus.PENDING)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .subtotal(calculation.getSubtotal())
                .tax(calculation.getTax())
                .discounts(calculation.getDiscounts())
                .total(calculation.getTotal())
                .shippingAddressId(request.getShippingAddressId())

                .shippingValue(roundedShippingValue)
                .notes(request.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private List<OrderItem> createOrderItems(CartResponseDTO cart, Order savedOrder) {
        return cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = OrderItem.builder()
                            .order(savedOrder)
                            .productId(cartItem.getProductId())
                            .productName(cartItem.getProductName())
                            .quantity(cartItem.getQuantity())
                            .productPrice(BigDecimal.valueOf(cartItem.getPrice()))
                            .subtotal(BigDecimal.valueOf(cartItem.getSubtotal()))
                            .build();
                    return orderItemRepository.save(orderItem);
                })
                .collect(Collectors.toList());
    }



    private void updateCartStatus(Long cartId, Long orderId) {
        try {
            cartClient.markCartAsConverted(orderId);
            log.info("Updated cart status to CONVERTED for cart: {} with order: {}", cartId, orderId);
        } catch (Exception e) {
            log.error("Failed to update cart status to CONVERTED for cart {} with order {}: {}", cartId, orderId, e.getMessage());
            throw new OrderCreationException("Failed to update cart status");
        }
    }

    private void createOrderStatusHistory(Order order, String notes) {
        orderStatusHistoryRepository.save(OrderStatusHistory.builder()
                .order(order)
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .notes(notes)
                .createdBy("ORDER_SERVICE")
                .createdAt(LocalDateTime.now())
                .build());
    }

    private void markCouponAsUsed(String couponCode, Long orderId) {
        try {
            CouponClient.UseCouponRequest couponRequest = new CouponClient.UseCouponRequest(couponCode, orderId);
            couponClient.useCoupon(couponRequest);
            log.info("Marked coupon {} as used for order {}", couponCode, orderId);
        } catch (Exception e) {
            log.error("Failed to mark coupon {} as used: {}", couponCode, e.getMessage());
            // Don't fail the order creation if coupon marking fails
        }
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
    }

    private void validateOrderOwnership(Order order) {
        Long currentUserId = currentUserService.getCurrentUserId();
        if (!order.getUserId().equals(currentUserId.toString())) {
            throw new AccessDeniedException("Access denied to order: " + order.getId());
        }
    }

    private void validateOrderCanBeCancelled(Order order) {
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new CartValidationException("Order cannot be cancelled. Current status: " + order.getStatus());
        }
    }

    private void validateOrderStatusTransition(Order.OrderStatus currentStatus, Order.OrderStatus newStatus) {
        // Define valid status transitions
        if (currentStatus == Order.OrderStatus.CANCELLED) {
            throw new CartValidationException("Cannot change status of a cancelled order");
        }
        
        if (currentStatus == Order.OrderStatus.DELIVERED && newStatus != Order.OrderStatus.DELIVERED) {
            throw new CartValidationException("Cannot change status of a delivered order");
        }
        
        // Validate specific transitions
        switch (newStatus) {
            case CONFIRMED:
                if (currentStatus != Order.OrderStatus.PENDING) {
                    throw new CartValidationException("Only pending orders can be confirmed");
                }
                break;
            case PROCESSING:
                if (currentStatus != Order.OrderStatus.CONFIRMED) {
                    throw new CartValidationException("Only confirmed orders can be processed");
                }
                break;
            case SHIPPED:
                if (currentStatus != Order.OrderStatus.PROCESSING) {
                    throw new CartValidationException("Only processing orders can be shipped");
                }
                break;
            case DELIVERED:
                if (currentStatus != Order.OrderStatus.SHIPPED) {
                    throw new CartValidationException("Only shipped orders can be marked as delivered");
                }
                break;
            case CANCELLED:
                if (currentStatus != Order.OrderStatus.PENDING && currentStatus != Order.OrderStatus.CONFIRMED) {
                    throw new CartValidationException("Only pending or confirmed orders can be cancelled");
                }
                break;
        }
    }

    private OrderResponseDTO buildOrderResponseDTO(Order order, List<OrderItem> orderItems, OrderCalculation calculation) {
        if (orderItems == null) {
            orderItems = orderItemRepository.findByOrderId(order.getId());
        }

        List<OrderItemDTO> orderItemDTOs = orderItems.stream()
                .map(item -> OrderItemDTO.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getProductPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        OrderCouponDTO couponDTO = null;
        if (calculation != null && calculation.getAppliedCoupon() != null) {
            couponDTO = OrderCouponDTO.builder()
                    .couponCode(calculation.getAppliedCoupon().getCode())
                    .couponName(calculation.getAppliedCoupon().getName())
                    .discountAmount(calculation.getDiscounts())
                    .discountType(calculation.getAppliedCoupon().getType().name())
                    .appliedAt(LocalDateTime.now())
                    .build();
        }

        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(Long.valueOf(order.getUserId()))
                .cartId(order.getCartId())
                .status(order.getStatus().name())
                .paymentStatus(order.getPaymentStatus().name())
                .subtotal(order.getSubtotal())
                .tax(order.getTax())
                .discounts(order.getDiscounts())
                .total(order.getTotal())
                .shippingValue(order.getShippingValue())
                .shippingAddressId(order.getShippingAddressId())

                .notes(order.getNotes())
                .items(orderItemDTOs)
                .appliedCoupon(couponDTO)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    // Helper class for order calculations
    private static class OrderCalculation {
        private final BigDecimal subtotal;
        private final BigDecimal tax;
        private final BigDecimal discounts;
        private final BigDecimal shippingValue;
        private final BigDecimal total;
        private final CouponDTO appliedCoupon;

        public OrderCalculation(BigDecimal subtotal, BigDecimal tax, 
                              BigDecimal discounts, BigDecimal shippingValue, BigDecimal total, CouponDTO appliedCoupon) {
            this.subtotal = subtotal;
            this.tax = tax;
            this.discounts = discounts;
            this.shippingValue = shippingValue;
            this.total = total;
            this.appliedCoupon = appliedCoupon;
        }

        public BigDecimal getSubtotal() { return subtotal; }
        public BigDecimal getTax() { return tax; }
        public BigDecimal getDiscounts() { return discounts; }
        public BigDecimal getShippingValue() { return shippingValue; }
        public BigDecimal getTotal() { return total; }
        public CouponDTO getAppliedCoupon() { return appliedCoupon; }
    }
}
