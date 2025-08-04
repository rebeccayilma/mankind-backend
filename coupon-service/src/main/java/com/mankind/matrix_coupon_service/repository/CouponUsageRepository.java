package com.mankind.matrix_coupon_service.repository;

import com.mankind.matrix_coupon_service.model.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {

    @Query("SELECT cu FROM CouponUsage cu WHERE cu.coupon.id = :couponId AND cu.userId = :userId")
    List<CouponUsage> findByCouponIdAndUserId(@Param("couponId") Long couponId, @Param("userId") Long userId);

    @Query("SELECT cu FROM CouponUsage cu WHERE cu.coupon.id = :couponId")
    List<CouponUsage> findByCouponId(@Param("couponId") Long couponId);

    @Query("SELECT cu FROM CouponUsage cu WHERE cu.userId = :userId")
    List<CouponUsage> findByUserId(@Param("userId") Long userId);

    @Query("SELECT cu FROM CouponUsage cu WHERE cu.coupon.id = :couponId AND cu.userId = :userId AND cu.orderId = :orderId")
    Optional<CouponUsage> findByCouponIdAndUserIdAndOrderId(
            @Param("couponId") Long couponId, 
            @Param("userId") Long userId, 
            @Param("orderId") String orderId);

    boolean existsByCouponIdAndUserIdAndOrderId(Long couponId, Long userId, String orderId);
} 