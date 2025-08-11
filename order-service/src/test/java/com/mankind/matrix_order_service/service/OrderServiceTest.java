package com.mankind.matrix_order_service.service;

import com.mankind.api.cart.dto.CartResponseDTO;
import com.mankind.api.cart.dto.CartItemResponseDTO;
import com.mankind.api.coupon.dto.CouponDTO;
import com.mankind.api.user.dto.UserDTO;
import com.mankind.api.user.dto.AddressDTO;
import com.mankind.matrix_order_service.client.CartClient;
import com.mankind.matrix_order_service.client.CouponClient;
import com.mankind.matrix_order_service.client.ProductClient;
import com.mankind.matrix_order_service.client.UserClient;
import com.mankind.matrix_order_service.dto.CreateOrderRequest;
import com.mankind.matrix_order_service.dto.OrderResponseDTO;
import com.mankind.matrix_order_service.exception.CartValidationException;
import com.mankind.matrix_order_service.exception.CouponValidationException;
import com.mankind.matrix_order_service.exception.OrderCreationException;
import com.mankind.matrix_order_service.model.Order;
import com.mankind.matrix_order_service.repository.OrderRepository;
import com.mankind.matrix_order_service.repository.OrderItemRepository;
import com.mankind.matrix_order_service.repository.OrderStatusHistoryRepository;
import com.mankind.matrix_order_service.repository.OrderPaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private OrderItemRepository orderItemRepository;
    
    @Mock
    private OrderStatusHistoryRepository orderStatusHistoryRepository;
    
    @Mock
    private OrderPaymentRepository orderPaymentRepository;
    
    @Mock
    private CartClient cartClient;
    
    @Mock
    private CouponClient couponClient;
    
    @Mock
    private ProductClient productClient;
    
    @Mock
    private UserClient userClient;
    
    @Mock
    private CurrentUserService currentUserService;
    
    @Mock
    private OrderNumberGenerator orderNumberGenerator;

    @InjectMocks
    private OrderService orderService;

    private CreateOrderRequest createOrderRequest;
    private CartResponseDTO cartResponse;
    private UserDTO userDTO;
    private AddressDTO shippingAddress;
    private AddressDTO billingAddress;
    private Order savedOrder;

    @BeforeEach
    void setUp() {
        // Setup test data
        createOrderRequest = CreateOrderRequest.builder()
                .shippingAddressId(1L)
                .billingAddressId(2L)
                .couponCode("SAVE20")
                .notes("Test order")
                .build();

        CartItemResponseDTO cartItem = CartItemResponseDTO.builder()
                .productId(1L)
                .productName("Test Product")
                .quantity(2)
                .price(100.0)
                .subtotal(200.0)
                .build();

        cartResponse = CartResponseDTO.builder()
                .id(1L)
                .userId(1L)
                .status("ACTIVE")
                .total(200.0)
                .items(Arrays.asList(cartItem))
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .email("test@example.com")
                .build();

        shippingAddress = AddressDTO.builder()
                .id(1L)
                .userId(1L)
                .addressType("shipping")
                .isDefault(true)
                .streetAddress("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("USA")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        billingAddress = AddressDTO.builder()
                .id(2L)
                .userId(1L)
                .addressType("billing")
                .isDefault(false)
                .streetAddress("456 Oak Ave")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("USA")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savedOrder = Order.builder()
                .id(1L)
                .orderNumber("ORD-20241201-143022-12345")
                .userId("1")
                .cartId(1L)
                .status(Order.OrderStatus.PENDING)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(200))
                .discountAmount(BigDecimal.ZERO)
                .finalAmount(BigDecimal.valueOf(200))
                .shippingAddressId(1L)
                .billingAddressId(2L)
                .notes("Test order")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createOrder_Success() {
        // Arrange
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(cartClient.getCurrentUserCart()).thenReturn(cartResponse);
        when(userClient.getAddressById(1L)).thenReturn(shippingAddress);
        when(userClient.getAddressById(2L)).thenReturn(billingAddress);
        when(orderNumberGenerator.generateOrderNumber()).thenReturn("ORD-20241201-143022-12345");
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.save(any())).thenReturn(null);
        when(orderStatusHistoryRepository.save(any())).thenReturn(null);
        when(cartClient.updateCartStatusToConverted(1L)).thenReturn(cartResponse);

        // Act
        OrderResponseDTO result = orderService.createOrder(createOrderRequest);

        // Assert
        assertNotNull(result);
        assertEquals("ORD-20241201-143022-12345", result.getOrderNumber());
        assertEquals(1L, result.getUserId());
        assertEquals("PENDING", result.getStatus());
        
        // Verify interactions
        verify(cartClient).getCurrentUserCart();
        verify(userClient).getAddressById(1L);
        verify(userClient).getAddressById(2L);
        verify(orderRepository).save(any(Order.class));
        verify(cartClient).updateCartStatusToConverted(1L);
        verify(productClient).markProductAsSold(1L, BigDecimal.valueOf(2));
    }

    @Test
    void createOrder_NoActiveCart_ThrowsException() {
        // Arrange
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(cartClient.getCurrentUserCart()).thenReturn(null);

        // Act & Assert
        CartValidationException exception = assertThrows(CartValidationException.class,
                () -> orderService.createOrder(createOrderRequest));
        assertEquals("No active cart found for current user", exception.getMessage());
    }

    @Test
    void createOrder_CartNotActive_ThrowsException() {
        // Arrange
        cartResponse.setStatus("CONVERTED");
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(cartClient.getCurrentUserCart()).thenReturn(cartResponse);

        // Act & Assert
        CartValidationException exception = assertThrows(CartValidationException.class,
                () -> orderService.createOrder(createOrderRequest));
        assertEquals("Cart is not active. Current status: CONVERTED", exception.getMessage());
    }

    @Test
    void createOrder_ShippingAddressNotOwnedByUser_ThrowsException() {
        // Arrange
        shippingAddress.setUserId(2L); // Different user
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(cartClient.getCurrentUserCart()).thenReturn(cartResponse);
        when(userClient.getAddressById(1L)).thenReturn(shippingAddress);

        // Act & Assert
        CartValidationException exception = assertThrows(CartValidationException.class,
                () -> orderService.createOrder(createOrderRequest));
        assertEquals("shipping address does not belong to current user", exception.getMessage());
    }

    @Test
    void createOrder_BillingAddressNotOwnedByUser_ThrowsException() {
        // Arrange
        billingAddress.setUserId(2L); // Different user
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(cartClient.getCurrentUserCart()).thenReturn(cartResponse);
        when(userClient.getAddressById(1L)).thenReturn(shippingAddress);
        when(userClient.getAddressById(2L)).thenReturn(billingAddress);

        // Act & Assert
        CartValidationException exception = assertThrows(CartValidationException.class,
                () -> orderService.createOrder(createOrderRequest));
        assertEquals("billing address does not belong to current user", exception.getMessage());
    }

    @Test
    void createOrder_WithValidCoupon_AppliesDiscount() {
        // Arrange
        CouponDTO coupon = CouponDTO.builder()
                .code("SAVE20")
                .name("Save 20%")
                .type(CouponDTO.CouponType.PERCENTAGE)
                .value(BigDecimal.valueOf(20))
                .build();

        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(cartClient.getCurrentUserCart()).thenReturn(cartResponse);
        when(userClient.getAddressById(1L)).thenReturn(shippingAddress);
        when(userClient.getAddressById(2L)).thenReturn(billingAddress);
        when(couponClient.validateCoupon("SAVE20")).thenReturn(coupon);
        when(orderNumberGenerator.generateOrderNumber()).thenReturn("ORD-20241201-143022-12345");
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.save(any())).thenReturn(null);
        when(orderStatusHistoryRepository.save(any())).thenReturn(null);
        when(cartClient.updateCartStatusToConverted(1L)).thenReturn(cartResponse);
        when(couponClient.useCoupon(any())).thenReturn(coupon);

        // Act
        OrderResponseDTO result = orderService.createOrder(createOrderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(40), result.getDiscountAmount()); // 20% of 200
        assertEquals(BigDecimal.valueOf(160), result.getFinalAmount()); // 200 - 40
        
        // Verify coupon interactions
        verify(couponClient).validateCoupon("SAVE20");
        verify(couponClient).useCoupon(any());
    }

    @Test
    void createOrder_WithInvalidCoupon_ThrowsException() {
        // Arrange
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(cartClient.getCurrentUserCart()).thenReturn(cartResponse);
        when(userClient.getAddressById(1L)).thenReturn(shippingAddress);
        when(userClient.getAddressById(2L)).thenReturn(billingAddress);
        when(couponClient.validateCoupon("INVALID")).thenThrow(new RuntimeException("Coupon not found"));

        // Act & Assert
        CouponValidationException exception = assertThrows(CouponValidationException.class,
                () -> orderService.createOrder(createOrderRequest));
        assertEquals("Invalid coupon code: SAVE20", exception.getMessage());
    }

    @Test
    void createOrder_InventoryUpdateFails_ThrowsException() {
        // Arrange
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(cartClient.getCurrentUserCart()).thenReturn(cartResponse);
        when(userClient.getAddressById(1L)).thenReturn(shippingAddress);
        when(userClient.getAddressById(2L)).thenReturn(billingAddress);
        when(orderNumberGenerator.generateOrderNumber()).thenReturn("ORD-20241201-143022-12345");
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.save(any())).thenReturn(null);
        when(productClient.markProductAsSold(1L, BigDecimal.valueOf(2)))
                .thenThrow(new RuntimeException("Inventory update failed"));

        // Act & Assert
        OrderCreationException exception = assertThrows(OrderCreationException.class,
                () -> orderService.createOrder(createOrderRequest));
        assertEquals("Failed to update inventory for product: 1", exception.getMessage());
    }

    @Test
    void createOrder_CartStatusUpdateFails_ThrowsException() {
        // Arrange
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(cartClient.getCurrentUserCart()).thenReturn(cartResponse);
        when(userClient.getAddressById(1L)).thenReturn(shippingAddress);
        when(userClient.getAddressById(2L)).thenReturn(billingAddress);
        when(orderNumberGenerator.generateOrderNumber()).thenReturn("ORD-20241201-143022-12345");
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.save(any())).thenReturn(null);
        when(productClient.markProductAsSold(1L, BigDecimal.valueOf(2))).thenReturn(null);
        when(cartClient.updateCartStatusToConverted(1L))
                .thenThrow(new RuntimeException("Cart update failed"));

        // Act & Assert
        OrderCreationException exception = assertThrows(OrderCreationException.class,
                () -> orderService.createOrder(createOrderRequest));
        assertEquals("Failed to update cart status", exception.getMessage());
    }
}
