package com.smart.mall.web;

import com.smart.mall.exception.http.NotFoundException;
import com.smart.mall.model.Banner;
import com.smart.mall.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/banner")
@Validated
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @GetMapping("/name/{name}")
    public Banner getByName(@PathVariable @NotBlank String name){
        Banner banner = this.bannerService.getByName(name);
        if(banner == null){
            throw new NotFoundException(4004);
        }
        return banner;
    }

}
