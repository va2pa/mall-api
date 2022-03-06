package com.smart.mall.core.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 保留两位小数，银行家算法(四舍六入 五考虑)
 */
//@Component
public class HalfEvenRound implements MoneyDiscount {
    @Override
    public BigDecimal discount(BigDecimal original, BigDecimal discount) {
        BigDecimal actualMoney = original.multiply(discount);
        return actualMoney.setScale(2, RoundingMode.HALF_EVEN);
    }
}
