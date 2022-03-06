package com.smart.mall.repository;

import com.smart.mall.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkuRepository extends JpaRepository<Sku, Long> {
    List<Sku> findAllByIdIn(List<Long> ids);
}
