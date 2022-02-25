package com.smart.mall.web;

import com.smart.mall.exception.http.NotFoundException;
import com.smart.mall.model.Banner;
import com.smart.mall.model.Spu;
import com.smart.mall.service.BannerService;
import com.smart.mall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {
    @Autowired
    private SpuService spuService;

    @GetMapping("/id/{id}/detail")
    public Spu getDetail(@PathVariable @Positive Long id){
        Spu spu = this.spuService.getSpuById(id);
        if(spu == null){
            throw new NotFoundException(4006);
        }
        return spu;
    }

}
