package com.smart.mall.repository;

import com.smart.mall.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByOpenid(String openid);

    User findFirstById(Long id);

    @Transactional
    @Modifying
    @Query("update User u \n" +
            "set u.level = :level \n" +
            "where u.id = :uid")
    int updateUserLevel(Long uid, Integer level);

    User findFirstByIdAndLevel(Long id, Integer level);
}
