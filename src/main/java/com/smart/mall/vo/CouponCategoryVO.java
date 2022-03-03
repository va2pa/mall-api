package com.smart.mall.vo;

import com.smart.mall.model.Coupon;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CouponCategoryVO extends CouponPureVO {
    private List<CategoryPureVO> categories;
    public CouponCategoryVO(Coupon coupon) {
        super(coupon);
        this.categories = coupon.getCategoryList().stream()
                .map(CategoryPureVO::new)
                .collect(Collectors.toList());
    }
}
