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

/**
 * 职责: 校验优惠劵数据:
 *        1. 优惠劵是否在可用时间内
 *        2. 优惠劵适用分类金额是否满足优惠劵使用门槛
 *        3. 使用优惠劵后的服务器最终价格是否和前端一致
 *
 */
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
     * 计算使用优惠劵后的服务器最终价格是否和前端一致
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
     * 优惠劵适用分类金额是否满足优惠劵使用门槛
     * @param skuOrderBOList
     * @param serverTotalPrice
     */
    public void canBeUsed(List<SkuOrderBO> skuOrderBOList, BigDecimal serverTotalPrice){
        // 先计算出订单中优惠券适用的商品总金额
        BigDecimal orderCategoryMoney = getOrderCategoryMoney(skuOrderBOList, serverTotalPrice);
        switch (CouponType.toType(this.coupon.getType())){
            case FULL_OFF:
            case FULL_MINUS:
                // 优惠券类型是满减券或满减折扣券，如果门槛金额大于订单适用金额，则抛出异常提示
                if(this.coupon.getFullMoney().compareTo(orderCategoryMoney) > 0){
                    // mall.codes[7008] = 商品金额未达到优惠劵使用门槛
                    throw new ParameterException(7008);
                }
                break;
                // 优惠券类型是无门槛券，则跳出，表示可以适用
            case NO_THRESHOLD_MINUS:
                break;
                // 系统中没有找到前端传来的优惠券类型，则抛出异常提示
            default:
                // mall.codes[6005] = 该类型优惠劵不存在
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
            // 通用券（无视分类）直接返回订单总价
            return serverTotalPrice;
        }
        BigDecimal orderCategoryMoney = new BigDecimal("0");
        // 遍历订单中所有sku
        for (SkuOrderBO skuOrderBO : skuOrderBOList) {
            // 遍历优惠券使用的所有分类
            for (Category category : coupon.getCategoryList()) {
                if (skuOrderBO.getCategoryId().equals(category.getId())
                    || skuOrderBO.getRootCategoryId().equals(category.getId())){
                    // 如果当前sku的二级分类或根分类与当前优惠券适用分类相同，则累加该sku价格
                    orderCategoryMoney = orderCategoryMoney.add(skuOrderBO.getTotalPrice());
                    // 跳出当前循环，遍历下一个sku
                    break;
                }
            }
        }
        return orderCategoryMoney;
    }
}
