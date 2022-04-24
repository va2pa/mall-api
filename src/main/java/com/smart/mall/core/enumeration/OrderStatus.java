package com.smart.mall.core.enumeration;

import java.util.stream.Stream;

public enum OrderStatus {
    All(0, "全部"),
    UNPAID(1, "待支付"),
    PAID(2, "已支付"),
    DELIVERED(3, "已发货"),
    FINISHED(4, "已完成"),
    CANCELED(5, "已取消");

    private int value;

    OrderStatus(int value, String text) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static OrderStatus toType(int value) {
        return Stream.of(OrderStatus.values())
                .filter(c -> c.value == value)
                .findAny()
                .orElse(null);
    }
}

