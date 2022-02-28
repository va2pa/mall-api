package com.smart.mall.vo;

import com.smart.mall.model.Theme;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ThemeWithSpuVO {
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

    private List<SpuPureVO> spuList;

    public ThemeWithSpuVO(Theme theme){
        BeanUtils.copyProperties(theme, this);
        List<SpuPureVO> SpuPureList = theme.getSpuList().stream()
                .map(SpuPureVO::new)
                .collect(Collectors.toList());
        this.setSpuList(SpuPureList);
    }
}
