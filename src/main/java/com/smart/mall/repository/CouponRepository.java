package com.smart.mall.repository;

import com.smart.mall.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("select c from Coupon c\n" +
            "join c.categoryList ca\n" +
            "join Activity a on a.id = c.activityId\n" +
            "where :cid = ca.id\n" +
            "and :now > a.startTime\n" +
            "and :now < a.endTime")
    List<Coupon> findByCategory(Long cid, Date now);

    @Query("select c from Coupon c\n" +
            "join Activity a on c.activityId = a.id\n" +
            "where c.wholeStore = :wholeStore\n" +
            "and :now > a.startTime\n" +
            "and :now < a.endTime\n")
    List<Coupon> findByWholeStore(boolean wholeStore, Date now);
}
