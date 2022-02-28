package com.smart.mall.repository;

import com.smart.mall.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByIsRootOrderByIndexAsc(Boolean isRoot);
}
