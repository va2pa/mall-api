package com.smart.mall.repository;

import com.smart.mall.model.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByStatusAndExpiredTimeGreaterThanAndUserId(Integer status, Date now, Long uid, Pageable pageable);

    Page<Order> findByUserId(Long uid, Pageable pageable);

    Page<Order> findByStatusAndUserId(Integer status,  Long uid, Pageable pageable);

    Optional<Order> findFirstByIdAndUserId(Long oid, Long uid);
}
