package com.smart.mall.web;

import com.smart.mall.core.interceptors.ScopeLevel;
import com.smart.mall.exception.http.NotFoundException;
import com.smart.mall.model.Activity;
import com.smart.mall.service.ActivityService;
import com.smart.mall.vo.ActivityCouponVO;
import com.smart.mall.vo.ActivityPureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.smart.mall.core.enumeration.AccessLevel.VIP;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping("/name/{name}")
    public ActivityPureVO getHomeActivity(@PathVariable String name){
        Activity activity = this.activityService.getByName(name)
                .orElseThrow(() -> new NotFoundException(4012));
        return new ActivityPureVO(activity);
    }

    @GetMapping("/name/{name}/with_coupon")
    public ActivityPureVO getActivityWithCoupon(@PathVariable String name){
        Activity activity = this.activityService.getByName(name)
                .orElseThrow(() -> new NotFoundException(4012));
        return new ActivityCouponVO(activity);
    }

    @ScopeLevel(VIP)
    @GetMapping("/name/{name}/with_coupon/vip")
    public ActivityPureVO getVipActivityWithCoupon(@PathVariable String name){
        Activity activity = this.activityService.getByName(name)
                .orElseThrow(() -> new NotFoundException(4012));
        return new ActivityCouponVO(activity);
    }
}
