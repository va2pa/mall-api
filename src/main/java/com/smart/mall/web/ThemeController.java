package com.smart.mall.web;

import com.smart.mall.exception.http.NotFoundException;
import com.smart.mall.model.Theme;
import com.smart.mall.service.ThemeService;
import com.smart.mall.vo.SpuPureVO;
import com.smart.mall.vo.ThemePureVO;
import com.smart.mall.vo.ThemeWithSpuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/theme")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @GetMapping("/by/names")
    public List<ThemePureVO> getThemeListByNames(@RequestParam String names){
        List<String> nameList = Arrays.asList(names.split(","));
        List<Theme> themeList = this.themeService.getTheListByNames(nameList);
        return themeList.stream()
                .map(ThemePureVO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/name/{name}/with_spu")
    public ThemeWithSpuVO getThemeByNameWithSpu(@PathVariable String name){
        Theme theme = this.themeService.getThemeByNameWithSpu(name)
                                    .orElseThrow(() -> new NotFoundException(4008));
        return new ThemeWithSpuVO(theme);
    }
}
