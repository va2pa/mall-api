package com.smart.mall.repository;

import com.smart.mall.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    Optional<UserCoupon> findByUserIdAndCouponId(Long uid, Long couponId);
}
