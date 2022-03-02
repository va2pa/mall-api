package com.smart.mall.web;

import com.smart.mall.core.LocalUser;
import com.smart.mall.core.interceptors.ScopeLevel;
import com.smart.mall.model.Coupon;
import com.smart.mall.service.CouponService;
import com.smart.mall.vo.CouponPureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        return coupons.stream()
                .map(CouponPureVO::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取全场劵（在所属活动范围内/可领取时间内）
     * @return
     */
    @GetMapping("/whole_store")
    public List<CouponPureVO> getWholeStore(){
        List<Coupon> coupons = this.couponService.getWholeStore();
        return coupons.stream()
                .map(CouponPureVO::new)
                .collect(Collectors.toList());
    }

    @ScopeLevel
    @PostMapping("/collect/{id}")
    public List<CouponPureVO> collectCoupon(@PathVariable Long id){
        Long uid = LocalUser.getUser().getId();
        return null;
    }
}
