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

import java.util.HashMap;
import java.util.Map;

import static com.smart.mall.core.enumeration.AccessLevel.LOGIN_USER;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ScopeLevel(LOGIN_USER)
    @PostMapping("/place")
    public OrderIdVO placeOrder(@RequestBody OrderDTO orderDTO){
        long uid = LocalUser.getUser().getId();
        // 校验orderDTO数据是否合法（与数据库对比）
        OrderCheck orderCheck = this.orderService.isOk(uid, orderDTO);
        Long orderId = this.orderService.placeOrder(uid, orderDTO, orderCheck);
        return new OrderIdVO(orderId);
    }

    @ScopeLevel(LOGIN_USER)
    @GetMapping("/status/unpaid")
    public PagingDozer<Order, OrderPureVO> getUnpaid(@RequestParam(defaultValue = "0") Integer start,
                                                     @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Long uid = LocalUser.getUser().getId();
        Page<Order> orderPage = this.orderService.getUnpaid(pageCounter.getPage(), pageCounter.getSize(), uid);
        return new PagingDozer<>(orderPage, OrderPureVO.class);
    }

    @ScopeLevel(LOGIN_USER)
    @GetMapping("/status/canceled")
    public PagingDozer<Order, OrderPureVO> getCanceled(@RequestParam(defaultValue = "0") Integer start,
                                                     @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Long uid = LocalUser.getUser().getId();
        Page<Order> orderPage = this.orderService.getCanceled(pageCounter.getPage(), pageCounter.getSize(), uid);
        return new PagingDozer<>(orderPage, OrderPureVO.class);
    }

    /**
     * 查询全部、已支付、已发货、已完成订单
     * @param status
     * @param start
     * @param count
     * @return
     */
    @ScopeLevel(LOGIN_USER)
    @GetMapping("by/status/{status}")
    public PagingDozer<Order, OrderPureVO> getByStatus(@PathVariable Integer status,
                                                     @RequestParam(defaultValue = "0") Integer start,
                                                     @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Long uid = LocalUser.getUser().getId();
        Page<Order> orderPage = this.orderService.getByStatus(status, pageCounter.getPage(), pageCounter.getSize(), uid);
        return new PagingDozer<>(orderPage, OrderPureVO.class);
    }

    @ScopeLevel(LOGIN_USER)
    @GetMapping("/detail/{oid}")
    public Order getDetail(@PathVariable Long oid){
        Long uid = LocalUser.getUser().getId();
        return this.orderService.getDetail(oid, uid)
                .orElseThrow(() -> new ParameterException(4014));
    }

    @ScopeLevel(LOGIN_USER)
    @PostMapping("/fakepay/{oid}")
    public Map<String, String> payOrder(@PathVariable Long oid){
        this.orderService.fakePay(oid);
        Map<String, String> map = new HashMap<>();
        map.put("fakepay","success");
        return map;
    }
}
