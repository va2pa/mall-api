package com.smart.mall.vo;

import com.smart.mall.model.Theme;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class ThemePureVO {
    private Long id;
    private String title;
    private String description;
    private String name;
    private String tplName;
    private String entranceImg;
    private String extend;
    private String internalTopImg;
    private String titleImg;
    private Boolean online;

    public ThemePureVO(Theme theme){
        BeanUtils.copyProperties(theme, this);
    }
}
