package com.smart.mall.service;

import com.smart.mall.model.Coupon;
import com.smart.mall.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public List<Coupon> getCouponsByCategory(Long cid){
        return this.couponRepository.findByCategory(cid, new Date());
    }

    public List<Coupon> getWholeStore() {
        return this.couponRepository.findByWholeStore(true, new Date());
    }
}
