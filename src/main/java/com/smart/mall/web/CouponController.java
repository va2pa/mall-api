package com.smart.mall.web;

import com.smart.mall.core.LocalUser;
import com.smart.mall.core.UnifyResponse;
import com.smart.mall.core.enumeration.CouponStatus;
import com.smart.mall.core.interceptors.ScopeLevel;
import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.model.Coupon;
import com.smart.mall.service.CouponService;
import com.smart.mall.vo.CouponCategoryVO;
import com.smart.mall.vo.CouponPureVO;
import jdk.nashorn.internal.objects.annotations.Getter;
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

    /**
     * 领取优惠劵
     * @param couponId
     */
    @ScopeLevel
    @PostMapping("/collect/{couponId}")
    public void collectCoupon(@PathVariable Long couponId){
        Long uid = LocalUser.getUser().getId();
        this.couponService.collectCoupon(uid, couponId);
        UnifyResponse.createSuccess();
    }

    /**
     * 查询我的三种状态的优惠劵：
     * 1：可使用，未过期
     * 2：已使用，未过期
     * 3：未使用，已过期
     * @param status
     * @return
     */
    @ScopeLevel
    @GetMapping("/myself/by/status/{status}")
    public List<CouponPureVO> getMyCouponByStatus(@PathVariable Integer status){
        Long uid = LocalUser.getUser().getId();
        List<Coupon> couponList;
        switch (CouponStatus.toType(status)){
            case AVAILABLE:
                couponList = this.couponService.getMyAvailableCoupons(uid);
                break;
            case USED:
                couponList = this.couponService.getMyUsedCoupons(uid);
                break;
            case EXPIRED:
                couponList = this.couponService.getMyExpiredCoupons(uid);
                break;
            default:
                throw new ParameterException(6012);
        }
        return CouponPureVO.getList(couponList);
    }

    /**
     * 查询我可用的优惠劵（带分类）
     * 应用于订单结算页面选择可用优惠劵
     * @return
     */
    @ScopeLevel
    @GetMapping("/myself/available/with_category")
    public List<CouponCategoryVO> getAvailableWithCategory(){
        Long uid = LocalUser.getUser().getId();
        List<Coupon> couponList = this.couponService.getMyAvailableCoupons(uid);
        return couponList.stream()
                .map(CouponCategoryVO::new)
                .collect(Collectors.toList());
    }
}
