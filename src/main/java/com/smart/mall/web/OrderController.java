package com.smart.mall.web;

import com.smart.mall.bo.PageCounter;
import com.smart.mall.core.LocalUser;
import com.smart.mall.core.interceptors.ScopeLevel;
import com.smart.mall.dto.OrderDTO;
import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.logic.OrderCheck;
import com.smart.mall.model.Order;
import com.smart.mall.service.OrderService;
import com.smart.mall.util.CommonUtil;
import com.smart.mall.vo.OrderIdVO;
import com.smart.mall.vo.OrderPureVO;
import com.smart.mall.vo.PagingDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ScopeLevel
    @PostMapping("/place")
    public OrderIdVO placeOrder(@RequestBody OrderDTO orderDTO){
        long uid = LocalUser.getUser().getId();
        OrderCheck orderCheck = this.orderService.isOk(uid, orderDTO);
        Long orderId = this.orderService.placeOrder(uid, orderDTO, orderCheck);
        return new OrderIdVO(orderId);
    }

    @ScopeLevel
    @GetMapping("/status/unpaid")
    public PagingDozer<Order, OrderPureVO> getUnpaid(@RequestParam(defaultValue = "0") Integer start,
                                                     @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Long uid = LocalUser.getUser().getId();
        Page<Order> orderPage = this.orderService.getUnpaid(pageCounter.getPage(), pageCounter.getSize(), uid);
        return new PagingDozer<>(orderPage, OrderPureVO.class);
    }

    /**
     * 查询全部、已支付、已发货、已完成订单
     * @param status
     * @param start
     * @param count
     * @return
     */
    @ScopeLevel
    @GetMapping("by/status/{status}")
    public PagingDozer<Order, OrderPureVO> getByStatus(@PathVariable Integer status,
                                                     @RequestParam(defaultValue = "0") Integer start,
                                                     @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Long uid = LocalUser.getUser().getId();
        Page<Order> orderPage = this.orderService.getByStatus(status, pageCounter.getPage(), pageCounter.getSize(), uid);
        return new PagingDozer<>(orderPage, OrderPureVO.class);
    }

    @ScopeLevel
    @GetMapping("/detail/{oid}")
    public Order getDetail(@PathVariable Long oid){
        Long uid = LocalUser.getUser().getId();
        return this.orderService.getDetail(oid, uid)
                .orElseThrow(() -> new ParameterException(4014));
    }
}
