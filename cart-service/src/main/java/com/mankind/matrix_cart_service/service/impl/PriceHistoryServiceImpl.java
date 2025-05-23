package com.mankind.matrix_cart_service.service.impl;

import com.mankind.matrix_cart_service.dto.PriceHistoryResponseDto;
import com.mankind.matrix_cart_service.mapper.PriceHistoryMapper;
import com.mankind.matrix_cart_service.model.CartItem;
import com.mankind.matrix_cart_service.model.PriceHistory;
import com.mankind.matrix_cart_service.repository.PriceHistoryRepository;
import com.mankind.matrix_cart_service.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceHistoryServiceImpl implements PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;
    private final PriceHistoryMapper priceHistoryMapper;

    @Override
    public List<PriceHistoryResponseDto> getPriceHistoryByCartItemId(Long cartItemId) {
        List<PriceHistory> priceHistories = priceHistoryRepository.findByCartItemIdOrderByChangeDateDesc(cartItemId);
        return priceHistoryMapper.toDtoList(priceHistories);
    }

    @Override
    @Transactional
    public void recordPriceChange(CartItem cartItem, BigDecimal oldPrice) {
        // Only record if the price has actually changed
        if (oldPrice != null && cartItem.getPriceAtAddition() != null && 
            oldPrice.compareTo(cartItem.getPriceAtAddition()) != 0) {

            PriceHistory priceHistory = PriceHistory.builder()
                    .cartItemId(cartItem.getId())
                    .oldPrice(oldPrice)
                    .newPrice(cartItem.getPriceAtAddition())
                    .build();

            priceHistoryRepository.save(priceHistory);
        }
    }
}
