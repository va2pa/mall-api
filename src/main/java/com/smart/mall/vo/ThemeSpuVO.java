package com.smart.mall.vo;

import com.smart.mall.model.Theme;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ThemeSpuVO extends ThemePureVO{

    private List<SpuPureVO> spus;

    public ThemeSpuVO(Theme theme){
        super(theme);
        this.spus = theme.getSpuList().stream()
                .map(SpuPureVO::new)
                .collect(Collectors.toList());
    }
}
