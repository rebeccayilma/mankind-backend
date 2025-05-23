package com.mankind.matrix_cart_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    private Long productId;
    private Integer quantity;

    // Cache product information for display
    @Column(length = 255)
    private String productName;

    @Column(length = 1000)
    private String productImageUrl;

    // Price tracking
    @Column(name = "price_at_addition", precision = 10, scale = 2, nullable = false)
    private BigDecimal priceAtAddition;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    // Save for later functionality
    private boolean savedForLater;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        // No need to check for null as savedForLater is a primitive boolean with default value false
        this.calculateTotalPrice();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.calculateTotalPrice();
    }

    /**
     * Calculate the total price based on price at addition and quantity
     */
    public void calculateTotalPrice() {
        if (this.priceAtAddition != null && this.quantity != null) {
            this.totalPrice = this.priceAtAddition.multiply(BigDecimal.valueOf(this.quantity));
        }
    }
}
