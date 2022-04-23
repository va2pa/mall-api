package com.smart.mall.logic;

import com.smart.mall.bo.SkuOrderBO;
import com.smart.mall.dto.OrderDTO;
import com.smart.mall.dto.SkuInfoDTO;
import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.model.OrderSku;
import com.smart.mall.model.Sku;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 职责: 1. 校验订单数据:
 *          1. sku是否已下架
 *          2. sku是否售罄
 *          3. sku是否超卖
 *          4. sku是否超出最大购买数量
 *          5. 前端总价格是否与服务器一致
 *          6. 优惠劵校验
*       2. 提供Order模型部分数据
 *          1. totalCount
 *          2. snapImg
 *          3. snapTitle
 *          4. snapItems (orderSkuList)
 */
public class OrderCheck {
    private OrderDTO orderDTO;
    private List<Sku> serverSkuList;
    private CouponCheck couponCheck;
    private Integer maxSkuLimit;
    @Getter
    private List<OrderSku> orderSkuList = new ArrayList<>();

    public OrderCheck(OrderDTO orderDTO, List<Sku> serverSkuList, CouponCheck couponCheck, Integer maxSkuLimit) {
        this.orderDTO = orderDTO;
        this.serverSkuList = serverSkuList;
        this.couponCheck = couponCheck;
        this.maxSkuLimit = maxSkuLimit;
    }

    public String getSnapImg() {
        return  this.serverSkuList.get(0).getImg();
    }

    public String getSnapTitle() {
        return this.serverSkuList.get(0).getTitle();
    }

    public Integer getTotalCount() {
        return this.orderDTO.getSkuInfoList().stream()
                .map(SkuInfoDTO::getCount)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public void isOk(){
        this.orderSkuCountIsOk();
        BigDecimal serverTotalPrice = new BigDecimal("0");
        List<SkuOrderBO> skuOrderBOList = new ArrayList<>();
        //保证列表遍历时前后端对应同一个sku
        Collections.sort(this.orderDTO.getSkuInfoList(), new Comparator<SkuInfoDTO>() {
            @Override
            public int compare(SkuInfoDTO o1, SkuInfoDTO o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        for(int i = 0;i < this.serverSkuList.size();i ++){
            Sku sku = this.serverSkuList.get(i);
            SkuInfoDTO skuInfoDTO = this.orderDTO.getSkuInfoList().get(i);
            skuNotOnSale();
            this.skuNotOnSale();
            this.containsSoldOutSku(sku);
            this.beyondSkuStock(skuInfoDTO, sku);
            this.beyondSkuMaxLimit(skuInfoDTO);
            //为couponCheck准备数据
            SkuOrderBO skuOrderBO = new SkuOrderBO(sku, skuInfoDTO);
            skuOrderBOList.add(skuOrderBO);
            serverTotalPrice = serverTotalPrice.add(skuOrderBO.getTotalPrice());
            //为order准备数据
            this.orderSkuList.add(new OrderSku(sku, skuInfoDTO));
        }
        totalPriceIsOk(this.orderDTO.getTotalPrice(), serverTotalPrice);
        if(this.orderDTO.getCouponId() != null){
            this.couponCheck.inTimeLine();
            this.couponCheck.canBeUsed(skuOrderBOList, serverTotalPrice);
            this.couponCheck.finalTotalPriceIsOk(this.orderDTO.getFinalTotalPrice(), serverTotalPrice);
        }
    }

    private void orderSkuCountIsOk(){
        if(this.serverSkuList.size() <= 0){
            throw new ParameterException(7003);
        }
    }

    /**
     * 前端总价格是否与服务器一致
     * @param totalPrice
     * @param serverTotalPrice
     */
    private void totalPriceIsOk(BigDecimal totalPrice, BigDecimal serverTotalPrice){
        if(totalPrice.compareTo(serverTotalPrice) != 0){
            throw new ParameterException(7005);
        }
    }

    private void skuNotOnSale(){
        if (this.serverSkuList.size() != this.orderDTO.getSkuInfoList().size()){
            throw new ParameterException(7504);
        }
    }

    private void containsSoldOutSku(Sku sku){
        if (sku.getStock() <= 0){
            throw new ParameterException(7506);
        }
    }

    private void beyondSkuStock(SkuInfoDTO skuInfoDTO, Sku sku){
        if (skuInfoDTO.getCount() > sku.getStock()){
            throw new ParameterException(7508);
        }
    }

    private void beyondSkuMaxLimit(SkuInfoDTO skuInfoDTO){
        if (skuInfoDTO.getCount() > this.maxSkuLimit){
            throw new ParameterException(7510);
        }
    }
}
