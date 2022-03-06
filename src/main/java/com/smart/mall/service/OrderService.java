package com.smart.mall.service;

import com.smart.mall.core.money.MoneyDiscount;
import com.smart.mall.dto.OrderDTO;
import com.smart.mall.dto.SkuInfoDTO;
import com.smart.mall.exception.http.NotFoundException;
import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.logic.CouponCheck;
import com.smart.mall.model.Coupon;
import com.smart.mall.model.Sku;
import com.smart.mall.model.UserCoupon;
import com.smart.mall.repository.CouponRepository;
import com.smart.mall.repository.SkuRepository;
import com.smart.mall.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private SkuRepository skuRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private MoneyDiscount moneyDiscount;
    @Value("${mall.order.max-sku-limit}")
    private Integer maxSkuLimit;

    public void isOk(Long uid, OrderDTO orderDTO){
        List<Long> skuIdList = orderDTO.getSkuInfoList().stream()
                .map(SkuInfoDTO::getId)
                .collect(Collectors.toList());
        List<Sku> skuList = skuRepository.findAllByIdIn(skuIdList);
        Long couponId = orderDTO.getCouponId();
        //该笔订单是否使用了优惠劵，如果有，则校验优惠劵
        if(couponId != null){
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new NotFoundException(6004));
            List<Coupon> myAvailable = couponRepository.findMyAvailable(uid, new Date());
            if (!myAvailable.contains(coupon)){
                throw new ParameterException(6016);
            }
            CouponCheck couponCheck = new CouponCheck(coupon, moneyDiscount);
        }


    }
}
