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
                .price(19.99)
                .build();
    }

    @Test
    void shouldReturnBadRequestWhenCartIdIsNull() throws Exception {
        // Arrange
        CartItemRequestDto invalidDto = CartItemRequestDto.builder()
                .cartId(null) // Invalid: cartId is null
                .productId(validRequestDto.getProductId())
                .quantity(validRequestDto.getQuantity())
                .price(validRequestDto.getPrice())
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
                .price(validRequestDto.getPrice())
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
    void shouldReturnBadRequestWhenQuantityIsZero() throws Exception {
        // Arrange
        CartItemRequestDto invalidDto = CartItemRequestDto.builder()
                .cartId(validRequestDto.getCartId())
                .productId(validRequestDto.getProductId())
                .quantity(0) // Invalid: quantity is zero
                .price(validRequestDto.getPrice())
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
                .price(validRequestDto.getPrice())
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
    void shouldReturnBadRequestWhenPriceIsZero() throws Exception {
        // Arrange
        CartItemRequestDto invalidDto = CartItemRequestDto.builder()
                .cartId(validRequestDto.getCartId())
                .productId(validRequestDto.getProductId())
                .quantity(validRequestDto.getQuantity())
                .price(0) // Using 0 to test price validation
                .build();
        String requestBody = objectMapper.writeValueAsString(invalidDto);

        // Act & Assert
        mockMvc.perform(post("/api/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
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
                .price(validRequestDto.getPrice())
                .build();

        when(cartItemService.createCartItem(any(CartItemRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }
}