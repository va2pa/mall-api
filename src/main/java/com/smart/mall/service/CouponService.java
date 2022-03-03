package com.smart.mall.service;

import com.smart.mall.core.enumeration.CouponStatus;
import com.smart.mall.exception.http.NotFoundException;
import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.model.Activity;
import com.smart.mall.model.Coupon;
import com.smart.mall.model.UserCoupon;
import com.smart.mall.repository.ActivityRepository;
import com.smart.mall.repository.CouponRepository;
import com.smart.mall.repository.UserCouponRepository;
import com.smart.mall.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    public List<Coupon> getCouponsByCategory(Long cid){
        return this.couponRepository.findByCategory(cid, new Date());
    }

    public List<Coupon> getWholeStore() {
        return this.couponRepository.findByWholeStore(true, new Date());
    }

    public void collectCoupon(Long uid, Long couponId){
        //该优惠劵是否存在
        this.couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundException(6004));
        //该优惠劵对应活动是否存在
        Activity activity = this.activityRepository.findFirstByCouponListId(couponId)
                .orElseThrow(() -> new NotFoundException(6006));
        //是否在活动时间内
        Date now = new Date();
        boolean inTimeLine = CommonUtil.isInTimeLine(now, activity.getStartTime(), activity.getEndTime());
        if (!inTimeLine){
            throw new ParameterException(6008);
        }
        //该用户是否领取过该优惠劵
        this.userCouponRepository.findByUserIdAndCouponId(uid, couponId)
                .ifPresent(t -> {throw new ParameterException(6010);});
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(uid)
                .couponId(couponId)
                .createTime(now)
                .status(CouponStatus.AVAILABLE.getValue())
                .build();
        this.userCouponRepository.save(userCoupon);
    }
}
