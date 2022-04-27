package com.smart.mall.repository;

import com.smart.mall.model.UserSpu;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserSpuRepository extends JpaRepository<UserSpu, Long> {
    UserSpu findFirstByUserIdAndSpuId(Long uid, Long sid);
}
