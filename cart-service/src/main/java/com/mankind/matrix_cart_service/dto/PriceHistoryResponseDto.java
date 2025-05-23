package com.mankind.matrix_cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistoryResponseDto {
    private Long id;
    private Long cartItemId;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private LocalDateTime changeDate;
}