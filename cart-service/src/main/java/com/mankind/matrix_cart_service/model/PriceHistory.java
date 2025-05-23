package com.mankind.matrix_cart_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistory {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_item_id")
    private Long cartItemId;

    @Column(precision = 10, scale = 2)
    private BigDecimal oldPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal newPrice;

    @Column(name = "change_date")
    private LocalDateTime changeDate;

    @PrePersist
    protected void onCreate() {
        this.changeDate = LocalDateTime.now();
    }
}