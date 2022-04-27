package com.smart.mall.repository;

import com.smart.mall.model.Spu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpuRepository extends JpaRepository<Spu, Long> {
    Spu findOneById(Long id);

    Page<Spu> findByCategoryId(Long cid, Pageable pageable);

    Page<Spu> findByRootCategoryId(Long cid, Pageable pageable);

    Page<Spu> findByTitleLike(String keyword, Pageable pageable);

    @Modifying
    @Query("update Spu s \n" +
            "set s.favorNum = s.favorNum + 1 \n" +
            "where s.id = :sid")
    int increaseFavorNum(Long sid);

    @Modifying
    @Query("update Spu s \n" +
            "set s.favorNum = s.favorNum - 1 \n" +
            "where s.id = :sid\n" +
            "and s.favorNum > 0")
    int decreaseFavorNum(Long sid);
}
