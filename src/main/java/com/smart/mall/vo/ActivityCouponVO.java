package com.smart.mall.vo;

import com.smart.mall.model.Activity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ActivityCouponVO extends ActivityPureVO{
    private List<CouponPureVO> coupons;

    public ActivityCouponVO(Activity activity){
        super(activity);
        this.coupons = activity.getCouponList()
                .stream()
                .map(CouponPureVO::new)
                .collect(Collectors.toList());
    }
}
