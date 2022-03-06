package com.smart.mall.web;

import com.smart.mall.core.LocalUser;
import com.smart.mall.core.interceptors.ScopeLevel;
import com.smart.mall.dto.OrderDTO;
import com.smart.mall.vo.OrderIdVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @ScopeLevel
    @PostMapping("")
    public OrderIdVO placeOrder(@RequestBody OrderDTO orderDTO){
        long uid = LocalUser.getUser().getId();
        return null;
    }
}
