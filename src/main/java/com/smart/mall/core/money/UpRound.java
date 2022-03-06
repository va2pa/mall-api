package com.smart.mall.core.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 保留两位小数，向上取整
 */
@Component
public class UpRound implements MoneyDiscount {
    @Override
    public BigDecimal discount(BigDecimal original, BigDecimal discount) {
        BigDecimal actualMoney = original.multiply(discount);
        return actualMoney.setScale(2, RoundingMode.UP);
    }
}
