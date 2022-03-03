package com.smart.mall.repository;

import com.smart.mall.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Optional<Activity> findByName(String name);

    Optional<Activity> findFirstByCouponListId(Long couponId);
}
