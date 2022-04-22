package com.smart.mall.web;

import com.smart.mall.bo.PageCounter;
import com.smart.mall.model.Sku;
import com.smart.mall.model.Spu;
import com.smart.mall.service.SkuService;
import com.smart.mall.util.CommonUtil;
import com.smart.mall.vo.PagingDozer;
import com.smart.mall.vo.SpuPureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sku")
public class SkuController {
    @Autowired
    private SkuService skuService;

    @GetMapping("")
    public List<Sku> getSkuListByIds(@RequestParam String ids){
        if (!StringUtils.hasLength(ids)){
            return Collections.emptyList();
        }
        String[] skuIds = ids.split(",");
        List<Long> skuIdList = Arrays.stream(skuIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.skuService.getSkuListByIds(skuIdList);
    }
}
