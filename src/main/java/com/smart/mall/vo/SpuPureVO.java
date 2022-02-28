package com.smart.mall.vo;

import com.smart.mall.model.Spu;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class SpuPureVO {
    private Long id;
    private String title;
    private String subtitle;
    private String price;
    private Long sketchSpecId;
    private String img;
    private String discountPrice;
    private String description;
    private String tags;
    private String forThemeImg;

    public SpuPureVO(Spu spu){
        BeanUtils.copyProperties(spu, this);
    }
}
