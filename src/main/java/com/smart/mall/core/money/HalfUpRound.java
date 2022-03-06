package com.smart.mall.core.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 保留两位小数，四舍五入
 */
//@Component
public class HalfUpRound implements MoneyDiscount {
    @Override
    public BigDecimal discount(BigDecimal original, BigDecimal discount) {
        BigDecimal actualMoney = original.multiply(discount);
        return actualMoney.setScale(2, RoundingMode.HALF_UP);
    }
}
