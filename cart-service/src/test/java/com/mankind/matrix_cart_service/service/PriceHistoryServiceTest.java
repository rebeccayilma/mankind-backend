package com.mankind.matrix_cart_service.service;

import com.mankind.matrix_cart_service.mapper.PriceHistoryMapper;
import com.mankind.matrix_cart_service.model.CartItem;
import com.mankind.matrix_cart_service.model.PriceHistory;
import com.mankind.matrix_cart_service.repository.PriceHistoryRepository;
import com.mankind.matrix_cart_service.service.impl.PriceHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceHistoryServiceTest {

    @Mock
    private PriceHistoryRepository priceHistoryRepository;

    @Mock
    private PriceHistoryMapper priceHistoryMapper;

    @InjectMocks
    private PriceHistoryServiceImpl priceHistoryService;

    private CartItem cartItem;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;

    @BeforeEach
    void setUp() {
        cartItem = CartItem.builder()
                .id(1L)
                .cart(null) // Will be set in a real scenario
                .productId(201L)
                .quantity(2)
                .productName("Test Product")
                .productImageUrl("http://example.com/image.jpg")
                .priceAtAddition(new BigDecimal("19.99"))
                .totalPrice(new BigDecimal("39.98"))
                .savedForLater(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        oldPrice = new BigDecimal("15.99");
        newPrice = cartItem.getPriceAtAddition();
    }

    @Test
    void shouldRecordPriceChangeWhenPriceHasChanged() {
        // Arrange
        when(priceHistoryRepository.save(any(PriceHistory.class))).thenReturn(new PriceHistory());

        // Act
        priceHistoryService.recordPriceChange(cartItem, oldPrice);

        // Assert
        verify(priceHistoryRepository, times(1)).save(any(PriceHistory.class));
    }

    @Test
    void shouldNotRecordPriceChangeWhenPriceHasNotChanged() {
        // Arrange
        BigDecimal samePrice = cartItem.getPriceAtAddition();

        // Act
        priceHistoryService.recordPriceChange(cartItem, samePrice);

        // Assert
        verify(priceHistoryRepository, never()).save(any(PriceHistory.class));
    }

    @Test
    void shouldNotRecordPriceChangeWhenOldPriceIsNull() {
        // Act
        priceHistoryService.recordPriceChange(cartItem, null);

        // Assert
        verify(priceHistoryRepository, never()).save(any(PriceHistory.class));
    }

    @Test
    void shouldNotRecordPriceChangeWhenNewPriceIsNull() {
        // Arrange
        cartItem.setPriceAtAddition(null);

        // Act
        priceHistoryService.recordPriceChange(cartItem, oldPrice);

        // Assert
        verify(priceHistoryRepository, never()).save(any(PriceHistory.class));
    }
}
