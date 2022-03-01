package com.smart.mall.repository;

import com.smart.mall.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByOpenid(String openid);
}
