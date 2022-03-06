package com.smart.mall.web;

import com.smart.mall.core.LocalUser;
import com.smart.mall.core.interceptors.ScopeLevel;
import com.smart.mall.dto.OrderDTO;
import com.smart.mall.logic.OrderCheck;
import com.smart.mall.service.OrderService;
import com.smart.mall.vo.OrderIdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ScopeLevel
    @PostMapping("")
    public OrderIdVO placeOrder(@RequestBody OrderDTO orderDTO){
        long uid = LocalUser.getUser().getId();
        OrderCheck orderCheck = this.orderService.isOk(uid, orderDTO);
        Long orderId = this.orderService.placeOrder(uid, orderDTO, orderCheck);
        return new OrderIdVO(orderId);
    }
}
