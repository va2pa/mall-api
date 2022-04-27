package com.smart.mall.vo;

import com.smart.mall.model.Spu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpuPureVO {
    private Long id;
    private String title;
    private String subtitle;
    private String price;
    private Long sketchSpecId;
    private String img;
    private String discountPrice;
    private String description;
    private String forThemeImg;

    public SpuPureVO(Spu spu){
        BeanUtils.copyProperties(spu, this);
    }
}
