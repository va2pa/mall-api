package com.smart.mall.repository;

import com.smart.mall.model.Coupon;
import com.smart.mall.model.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByStatusAndExpiredTimeGreaterThanAndUserId(Integer status, Date now, Long uid, Pageable pageable);

    Page<Order> findByUserId(Long uid, Pageable pageable);

    Page<Order> findByStatusAndUserId(Integer status,  Long uid, Pageable pageable);

    Optional<Order> findFirstByIdAndUserId(Long oid, Long uid);

    @Modifying
    @Query("update Order o \n" +
            "set o.status = 5 \n" +
            "where o.status = 1\n" +
            "and o.id = :oid")
    int cancelOrder(Long oid);

    @Transactional
    @Modifying
    @Query("update Order o \n" +
            "set o.status = :status \n" +
            "where o.id = :oid")
    int updateOrderStatus(Long oid, Integer status);

    @Query("select o from Order o\n" +
            "where o.userId = :uid\n" +
            "and o.status <> 2\n" +
            "and o.status <> 3\n" +
            "and o.status <> 4\n" +
            "and o.expiredTime < :now")
    Page<Order> findMyExpired(Long uid, Date now, Pageable pageable);

    @Query(value = "select * from `order` o\n" +
            "where o.user_id = :uid\n" +
            "and o.status <> 1\n" +
            "and o.status <> 5\n" +
            "and o.final_total_price >= :vipThreshold \n" +
            "limit 1", nativeQuery = true)
    Order findFirstFinalTotalPriceGreaterThan(Long uid, Long vipThreshold);
}
