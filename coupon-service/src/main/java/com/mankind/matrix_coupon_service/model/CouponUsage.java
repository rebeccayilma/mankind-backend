package com.mankind.matrix_coupon_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_usage")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private LocalDateTime usedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Composite unique constraint to prevent duplicate usage
    @Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"coupon_id", "userId", "orderId"})
    })
    public static class CouponUsageId {
        // This is just for the unique constraint annotation
    }
} 