package com.smart.mall.repository;

import com.smart.mall.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    Optional<UserCoupon> findByUserIdAndCouponId(Long uid, Long couponId);

    @Modifying
    @Query(value = "update user_coupon uc\n" +
            "join coupon c on uc.coupon_id = c.id\n" +
            "set uc.status = 2, uc.order_id = :orderId\n" +
            "where uc.user_id = :uid\n" +
            "and uc.coupon_id = :couponId\n" +
            "and uc.status = 1\n" +
            "and uc.order_id is null\n" +
            "and c.start_time < :now\n" +
            "and c.end_time > :now", nativeQuery = true)
    int writeOffCoupon(Long couponId, Long uid, Long orderId, Date now);

    @Modifying
    @Query("update UserCoupon uc\n" +
            "set uc.status = 1, uc.orderId = null \n" +
            "where uc.couponId = :couponId\n" +
            "and uc.userId = :uid\n" +
            "and uc.orderId is not null\n" +
            "and uc.status = 2")
    int returnBack(Long couponId, Long uid);
}
