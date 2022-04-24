package com.smart.mall.bo;

import com.smart.mall.dto.SkuInfoDTO;
import com.smart.mall.model.Sku;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SkuOrderBO {
    private BigDecimal actualPrice;     //商品有折扣则折扣价
    private Integer count;
    private Long categoryId;
    private Long rootCategoryId;

    public SkuOrderBO(Sku sku, SkuInfoDTO skuInfoDTO){
        this.actualPrice = sku.getActualPrice();
        this.count = skuInfoDTO.getCount();
        this.categoryId = sku.getCategoryId();
        this.rootCategoryId = sku.getRootCategoryId();
    }

    public BigDecimal getTotalPrice(){
        return this.actualPrice.multiply(new BigDecimal(this.count));
    }
}
