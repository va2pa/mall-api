package com.smart.mall.web;

import com.smart.mall.exception.http.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banner")
public class bannerController {

    @GetMapping("/test")
    public void test(){
        throw new NotFoundException(9999);
    }
    @GetMapping("/test2")
    public void test2(){
        throw new RuntimeException();
    }
}
