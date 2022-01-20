package com.smart.mall.web;

import com.smart.mall.exception.http.NotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping("/banner")
@Validated
public class bannerController {

    @GetMapping("/test")
    public void test(){
        throw new RuntimeException();
    }
}
