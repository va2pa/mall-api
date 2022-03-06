package com.smart.mall.logic;

import com.smart.mall.bo.SkuOrderBO;
import com.smart.mall.core.enumeration.CouponType;
import com.smart.mall.core.money.MoneyDiscount;
import com.smart.mall.exception.http.ForbiddenException;
import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.model.Category;
import com.smart.mall.model.Coupon;
import com.smart.mall.util.CommonUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CouponCheck {
    private Coupon coupon;
    private MoneyDiscount moneyDiscount;

    public CouponCheck(Coupon coupon, MoneyDiscount moneyDiscount){
        this.coupon = coupon;
        this.moneyDiscount = moneyDiscount;
    }

    /**
     * 优惠劵是否在可用时间内
     */
    public void inTimeLine(){
        Date now = new Date();
        boolean inTime = CommonUtil.isInTimeLine(now, this.coupon.getStartTime(), this.coupon.getEndTime());
        if (!inTime){
            throw new ForbiddenException(6014);
        }
    }

    /**
     * 计算服务器使用优惠劵后的最终价格是否和前端一致
     * @param orderFinalTotalPrice
     * @param serverTotalPrice
     */
    public void finalTotalPriceIsOk(BigDecimal orderFinalTotalPrice,
                                    BigDecimal serverTotalPrice){
        BigDecimal serverFinalTotalPrice;
        switch (CouponType.toType(this.coupon.getType())){
            case FULL_MINUS:
            case NO_THRESHOLD_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(this.coupon.getMinus());
                if (serverFinalTotalPrice.compareTo(new BigDecimal("0")) <= 0){
                    throw new ForbiddenException(7004);
                }
                break;
            case FULL_OFF:
                serverFinalTotalPrice = this.moneyDiscount.discount(serverTotalPrice, this.coupon.getRate());
                break;
            default:
                throw new ParameterException(6005);
        }

        if (serverFinalTotalPrice.compareTo(orderFinalTotalPrice) != 0){
            throw new ForbiddenException(7006);
        }
    }

    /**
     * 订单优惠劵对应分类金额是否满足满减优惠劵门槛
     * @param skuOrderBOList
     * @param serverTotalPrice
     */
    public void canBeUsed(List<SkuOrderBO> skuOrderBOList, BigDecimal serverTotalPrice){
        BigDecimal orderCategoryMoney = getOrderCategoryMoney(skuOrderBOList, serverTotalPrice);
        switch (CouponType.toType(this.coupon.getType())){
            case FULL_OFF:
            case FULL_MINUS:
                if(this.coupon.getFullMoney().compareTo(orderCategoryMoney) > 0){
                    throw new ParameterException(7008);
                }
                break;
            case NO_THRESHOLD_MINUS:
                break;
            default:
                throw new ParameterException(6005);
        }
    }

    /**
     * 计算订单优惠劵对应分类金额
     * @param skuOrderBOList
     * @param serverTotalPrice
     * @return
     */
    private BigDecimal getOrderCategoryMoney(List<SkuOrderBO> skuOrderBOList, BigDecimal serverTotalPrice){
        if (coupon.getWholeStore()){
            return serverTotalPrice;
        }
        BigDecimal orderCategoryMoney = new BigDecimal("0");
        for (SkuOrderBO skuOrderBO : skuOrderBOList) {
            for (Category category : coupon.getCategoryList()) {
                if (skuOrderBO.getCategoryId().equals(category.getId())){
                    orderCategoryMoney = orderCategoryMoney.add(skuOrderBO.getTotalPrice());
                    break;
                }
            }
        }
        return orderCategoryMoney;
    }
}
