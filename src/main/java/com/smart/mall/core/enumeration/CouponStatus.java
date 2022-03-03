package com.smart.mall.core.enumeration;

import java.util.stream.Stream;

public enum CouponStatus {
    AVAILABLE(1, "可使用，未过期"),
    USED(2, "已使用，未过期"),
    EXPIRED(3, "未使用，已过期");

    private Integer value;

    CouponStatus(Integer value, String description){
        this.value = value;
    }

    public Integer getValue(){
        return this.value;
    }

    public static CouponStatus toType(Integer value){
        return Stream.of(CouponStatus.values())
                .filter(t -> t.getValue() == value)
                .findAny()
                .orElse(null);
    }
}
