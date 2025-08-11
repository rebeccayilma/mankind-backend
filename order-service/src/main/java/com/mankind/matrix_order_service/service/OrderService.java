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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        // Generate order number and calculate totals
        String orderNumber = orderNumberGenerator.generateOrderNumber();
        OrderCalculation calculation = calculateOrderTotals(cart, request.getCouponCode());

        // Create and save order
        Order order = createOrderEntity(request, userId, cart, orderNumber, calculation);
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

    public List<OrderResponseDTO> getUserOrders() {
        Long userId = currentUserService.getCurrentUserId();
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId.toString());
        return orders.stream()
                .map(order -> buildOrderResponseDTO(order, null, null))
                .collect(Collectors.toList());
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
                throw new CartValidationException("Cannot connect to cart service. Service may not be running on port 8082.");
            } else {
                throw new CartValidationException("Failed to retrieve cart: " + e.getMessage());
            }
        }
    }

    private void validateAddresses(CreateOrderRequest request, Long userId) {
        validateAddressOwnership(request.getShippingAddressId(), "shipping", userId);
        validateAddressOwnership(request.getBillingAddressId(), "billing", userId);
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

    private OrderCalculation calculateOrderTotals(CartResponseDTO cart, String couponCode) {
        BigDecimal totalAmount = BigDecimal.valueOf(cart.getTotal());
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal finalAmount = totalAmount;
        CouponDTO appliedCoupon = null;

        if (couponCode != null && !couponCode.isEmpty()) {
            appliedCoupon = validateAndApplyCoupon(couponCode, totalAmount);
            discountAmount = calculateDiscount(appliedCoupon, totalAmount);
            finalAmount = totalAmount.subtract(discountAmount);
            if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
                finalAmount = BigDecimal.ZERO;
            }
        }

        return new OrderCalculation(totalAmount, discountAmount, finalAmount, appliedCoupon);
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
        if (coupon.getType() == CouponDTO.CouponType.PERCENTAGE) {
            return totalAmount.multiply(coupon.getValue()).divide(BigDecimal.valueOf(100));
        } else if (coupon.getType() == CouponDTO.CouponType.FIXED_AMOUNT) {
            return coupon.getValue();
        }
        return BigDecimal.ZERO;
    }

    private Order createOrderEntity(CreateOrderRequest request, Long userId, CartResponseDTO cart, 
                                  String orderNumber, OrderCalculation calculation) {
        return Order.builder()
                .orderNumber(orderNumber)
                .userId(userId.toString())
                .cartId(cart.getId())
                .status(Order.OrderStatus.PENDING)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .totalAmount(calculation.getTotalAmount())
                .discountAmount(calculation.getDiscountAmount())
                .finalAmount(calculation.getFinalAmount())
                .shippingAddressId(request.getShippingAddressId())
                .billingAddressId(request.getBillingAddressId())
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
                .notes(notes)
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
                    .discountAmount(calculation.getDiscountAmount())
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
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .shippingAddressId(order.getShippingAddressId())
                .billingAddressId(order.getBillingAddressId())
                .notes(order.getNotes())
                .items(orderItemDTOs)
                .appliedCoupon(couponDTO)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    // Helper class for order calculations
    private static class OrderCalculation {
        private final BigDecimal totalAmount;
        private final BigDecimal discountAmount;
        private final BigDecimal finalAmount;
        private final CouponDTO appliedCoupon;

        public OrderCalculation(BigDecimal totalAmount, BigDecimal discountAmount, 
                              BigDecimal finalAmount, CouponDTO appliedCoupon) {
            this.totalAmount = totalAmount;
            this.discountAmount = discountAmount;
            this.finalAmount = finalAmount;
            this.appliedCoupon = appliedCoupon;
        }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public BigDecimal getDiscountAmount() { return discountAmount; }
        public BigDecimal getFinalAmount() { return finalAmount; }
        public CouponDTO getAppliedCoupon() { return appliedCoupon; }
    }
}
