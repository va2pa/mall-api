package com.smart.mall.web;

import com.smart.mall.core.LocalUser;
import com.smart.mall.core.UnifyResponse;
import com.smart.mall.core.interceptors.ScopeLevel;
import com.smart.mall.model.Coupon;
import com.smart.mall.service.CouponService;
import com.smart.mall.vo.CouponPureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    /**
     * 获取适用于某二级分类的优惠劵（在所属活动范围内/可领取时间内）
     * @param cid
     * @return
     */
    @GetMapping("/by/category/{cid}")
    public List<CouponPureVO> getCouponsByCategory(@PathVariable Long cid){
        List<Coupon> coupons = this.couponService.getCouponsByCategory(cid);
        return CouponPureVO.getList(coupons);
    }

    /**
     * 获取全场劵（在所属活动范围内/可领取时间内）
     * @return
     */
    @GetMapping("/whole_store")
    public List<CouponPureVO> getWholeStore(){
        List<Coupon> coupons = this.couponService.getWholeStore();
        return CouponPureVO.getList(coupons);
    }

    @ScopeLevel
    @PostMapping("/collect/{couponId}")
    public void collectCoupon(@PathVariable Long couponId){
        Long uid = LocalUser.getUser().getId();
        this.couponService.collectCoupon(uid, couponId);
        UnifyResponse.createSuccess();
    }
}
