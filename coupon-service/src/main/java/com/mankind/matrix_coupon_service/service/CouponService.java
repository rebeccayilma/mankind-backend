package com.mankind.matrix_coupon_service.service;

import com.mankind.matrix_coupon_service.dto.CreateCouponRequest;
import com.mankind.matrix_coupon_service.model.Coupon;
import com.mankind.matrix_coupon_service.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;

    public List<Coupon> getActivePublicCoupons() {
        log.debug("Fetching active public coupons");
        return couponRepository.findActivePublicCoupons(LocalDateTime.now());
    }

    public Page<Coupon> getAllActiveCoupons(Pageable pageable) {
        log.debug("Fetching all active coupons with pagination");
        return couponRepository.findActiveCoupons(LocalDateTime.now(), pageable);
    }

    public Coupon getCouponById(Long id) {
        log.debug("Fetching coupon by ID: {}", id);
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
    }

    public Coupon validateCoupon(String code, Long userId) {
        log.debug("Validating coupon code: {} for user: {}", code, userId);
        Coupon coupon = couponRepository.findActiveCouponByCode(code, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Coupon not found or inactive: " + code));
        // Check if user has already used this coupon (if one-time use per user)
        if (coupon.getOneTimeUsePerUser()) {
            Long usageCount = couponRepository.countUsageByCouponAndUser(coupon.getId(), userId);
            if (usageCount > 0) {
                throw new RuntimeException("Coupon already used by this user: " + code);
            }
        }
        log.info("Coupon validated successfully: {} for user: {}", code, userId);
        return coupon;
    }

    public Coupon createCoupon(Coupon coupon) {
        log.debug("Creating new coupon: {}", coupon.getCode());
        if (couponRepository.existsByCode(coupon.getCode())) {
            throw new RuntimeException("Coupon code already exists: " + coupon.getCode());
        }
        coupon.setCurrentUsage(0);
        coupon.setIsActive(true);
        Coupon savedCoupon = couponRepository.save(coupon);
        log.info("Coupon created successfully: {}", savedCoupon.getCode());
        return savedCoupon;
    }

    public Coupon createCoupon(CreateCouponRequest request) {
        log.debug("Creating new coupon from request: {}", request.getCode());
        if (couponRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Coupon code already exists: " + request.getCode());
        }
        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode());
        coupon.setName(request.getName());
        coupon.setDescription(request.getDescription());
        coupon.setType(request.getType());
        coupon.setValue(request.getValue());
        coupon.setMinimumOrderAmount(request.getMinimumOrderAmount());
        coupon.setMaxUsage(request.getMaxUsage());
        coupon.setCurrentUsage(request.getCurrentUsage());
        coupon.setValidFrom(request.getValidFrom());
        coupon.setValidTo(request.getValidTo());
        coupon.setOneTimeUsePerUser(request.getOneTimeUsePerUser());
        coupon.setIsActive(true);
        Coupon savedCoupon = couponRepository.save(coupon);
        log.info("Coupon created successfully: {}", savedCoupon.getCode());
        return savedCoupon;
    }

    public Coupon updateCoupon(Long id, Coupon couponDetails) {
        log.debug("Updating coupon with ID: {}", id);
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
        if (!coupon.getCode().equals(couponDetails.getCode()) && 
            couponRepository.existsByCode(couponDetails.getCode())) {
            throw new RuntimeException("Coupon code already exists: " + couponDetails.getCode());
        }
        coupon.setCode(couponDetails.getCode());
        coupon.setName(couponDetails.getName());
        coupon.setDescription(couponDetails.getDescription());
        coupon.setType(couponDetails.getType());
        coupon.setValue(couponDetails.getValue());
        coupon.setMinimumOrderAmount(couponDetails.getMinimumOrderAmount());
        coupon.setMaxUsage(couponDetails.getMaxUsage());
        coupon.setValidFrom(couponDetails.getValidFrom());
        coupon.setValidTo(couponDetails.getValidTo());
        coupon.setIsActive(couponDetails.getIsActive());
        coupon.setOneTimeUsePerUser(couponDetails.getOneTimeUsePerUser());
        Coupon updatedCoupon = couponRepository.save(coupon);
        log.info("Coupon updated successfully: {}", updatedCoupon.getCode());
        return updatedCoupon;
    }

    public void deleteCoupon(Long id) {
        log.debug("Deleting coupon with ID: {}", id);
        couponRepository.deleteById(id);
    }
} 