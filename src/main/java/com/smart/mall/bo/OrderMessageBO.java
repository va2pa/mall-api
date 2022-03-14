package com.smart.mall.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMessageBO {
    private Long orderId;
    private Long userId;
    private Long couponId;

    public OrderMessageBO(String message) {
        String[] ss = message.split(",");
        this.orderId = Long.parseLong(ss[0]);
        this.userId = Long.parseLong(ss[1]);
        this.couponId = Long.parseLong(ss[2]);
    }
}
