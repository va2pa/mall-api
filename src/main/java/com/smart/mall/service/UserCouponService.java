package com.smart.mall.service;

import com.smart.mall.model.UserCoupon;
import com.smart.mall.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCouponService {
    @Autowired
    public UserCouponRepository userCouponRepository;

}
