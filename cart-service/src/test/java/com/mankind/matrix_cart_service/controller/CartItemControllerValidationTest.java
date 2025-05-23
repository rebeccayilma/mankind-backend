package com.mankind.matrix_cart_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mankind.matrix_cart_service.dto.CartItemRequestDto;
import com.mankind.matrix_cart_service.dto.CartItemResponseDto;
import com.mankind.matrix_cart_service.service.CartItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartItemController.class)
public class CartItemControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartItemService cartItemService;

    private CartItemRequestDto validRequestDto;

    @BeforeEach
    void setUp() {
        validRequestDto = CartItemRequestDto.builder()
                .cartId(101L)
                .productId(201L)
                .quantity(2)
                .productName("Test Product")
                .productImageUrl("http://example.com/image.jpg")
                .priceAtAddition(new BigDecimal("19.99"))
                .savedForLater(false)
                .build();
    }

    @Test
    void shouldReturnBadRequestWhenCartIdIsNull() throws Exception {
        // Arrange
        CartItemRequestDto invalidDto = CartItemRequestDto.builder()
                .cartId(null) // Invalid: cartId is null
                .productId(validRequestDto.getProductId())
                .quantity(validRequestDto.getQuantity())
                .productName(validRequestDto.getProductName())
                .productImageUrl(validRequestDto.getProductImageUrl())
                .priceAtAddition(validRequestDto.getPriceAtAddition())
                .savedForLater(validRequestDto.getSavedForLater())
                .build();
        String requestBody = objectMapper.writeValueAsString(invalidDto);

        // Act & Assert
        mockMvc.perform(post("/api/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.cartId").value("Cart ID cannot be null"));
    }

    @Test
    void shouldReturnBadRequestWhenProductIdIsNull() throws Exception {
        // Arrange
        CartItemRequestDto invalidDto = CartItemRequestDto.builder()
                .cartId(validRequestDto.getCartId())
                .productId(null) // Invalid: productId is null
                .quantity(validRequestDto.getQuantity())
                .productName(validRequestDto.getProductName())
                .productImageUrl(validRequestDto.getProductImageUrl())
                .priceAtAddition(validRequestDto.getPriceAtAddition())
                .savedForLater(validRequestDto.getSavedForLater())
                .build();
        String requestBody = objectMapper.writeValueAsString(invalidDto);

        // Act & Assert
        mockMvc.perform(post("/api/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.productId").value("Product ID cannot be null"));
    }

    @Test
    void shouldReturnBadRequestWhenQuantityIsNull() throws Exception {
        // Arrange
        CartItemRequestDto invalidDto = CartItemRequestDto.builder()
                .cartId(validRequestDto.getCartId())
                .productId(validRequestDto.getProductId())
                .quantity(null) // Invalid: quantity is null
                .productName(validRequestDto.getProductName())
                .productImageUrl(validRequestDto.getProductImageUrl())
                .priceAtAddition(validRequestDto.getPriceAtAddition())
                .savedForLater(validRequestDto.getSavedForLater())
                .build();
        String requestBody = objectMapper.writeValueAsString(invalidDto);

        // Act & Assert
        mockMvc.perform(post("/api/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.quantity").value("Quantity cannot be null"));
    }

    @Test
    void shouldReturnBadRequestWhenQuantityIsZero() throws Exception {
        // Arrange
        CartItemRequestDto invalidDto = CartItemRequestDto.builder()
                .cartId(validRequestDto.getCartId())
                .productId(validRequestDto.getProductId())
                .quantity(0) // Invalid: quantity is zero
                .productName(validRequestDto.getProductName())
                .productImageUrl(validRequestDto.getProductImageUrl())
                .priceAtAddition(validRequestDto.getPriceAtAddition())
                .savedForLater(validRequestDto.getSavedForLater())
                .build();
        String requestBody = objectMapper.writeValueAsString(invalidDto);

        // Act & Assert
        mockMvc.perform(post("/api/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.quantity").value("Quantity must be greater than zero"));
    }

    @Test
    void shouldReturnBadRequestWhenQuantityIsNegative() throws Exception {
        // Arrange
        CartItemRequestDto invalidDto = CartItemRequestDto.builder()
                .cartId(validRequestDto.getCartId())
                .productId(validRequestDto.getProductId())
                .quantity(-1) // Invalid: quantity is negative
                .productName(validRequestDto.getProductName())
                .productImageUrl(validRequestDto.getProductImageUrl())
                .priceAtAddition(validRequestDto.getPriceAtAddition())
                .savedForLater(validRequestDto.getSavedForLater())
                .build();
        String requestBody = objectMapper.writeValueAsString(invalidDto);

        // Act & Assert
        mockMvc.perform(post("/api/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.quantity").value("Quantity must be greater than zero"));
    }

    @Test
    void shouldReturnBadRequestWhenProductNameExceedsMaxLength() throws Exception {
        // Arrange
        String longName = "a".repeat(256); // 256 characters, exceeding the 255 limit
        CartItemRequestDto invalidDto = CartItemRequestDto.builder()
                .cartId(validRequestDto.getCartId())
                .productId(validRequestDto.getProductId())
                .quantity(validRequestDto.getQuantity())
                .productName(longName) // Invalid: productName exceeds max length
                .productImageUrl(validRequestDto.getProductImageUrl())
                .priceAtAddition(validRequestDto.getPriceAtAddition())
                .savedForLater(validRequestDto.getSavedForLater())
                .build();
        String requestBody = objectMapper.writeValueAsString(invalidDto);

        // Act & Assert
        mockMvc.perform(post("/api/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.productName").value("Product name cannot exceed 255 characters"));
    }

    @Test
    void shouldAcceptValidRequest() throws Exception {
        // Arrange
        String requestBody = objectMapper.writeValueAsString(validRequestDto);

        // Mock service response
        CartItemResponseDto responseDto = CartItemResponseDto.builder()
                .id(1L)
                .cartId(validRequestDto.getCartId())
                .productId(validRequestDto.getProductId())
                .quantity(validRequestDto.getQuantity())
                .productName(validRequestDto.getProductName())
                .productImageUrl(validRequestDto.getProductImageUrl())
                .priceAtAddition(validRequestDto.getPriceAtAddition())
                .totalPrice(new BigDecimal("39.98")) // 19.99 * 2
                .savedForLater(validRequestDto.getSavedForLater())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(cartItemService.createCartItem(any(CartItemRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }
}
