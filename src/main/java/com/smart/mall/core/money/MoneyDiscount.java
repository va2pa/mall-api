package com.smart.mall.core.money;

import java.math.BigDecimal;

public interface MoneyDiscount {
    BigDecimal discount(BigDecimal original, BigDecimal discount);
}
