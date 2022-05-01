package com.smart.mall.manager;


import com.smart.mall.bo.OrderMessageBO;
import com.smart.mall.service.CouponService;
import com.smart.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;


public class TopicMessageListener implements MessageListener {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CouponService couponService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        String expiredKey = new String(body);
        OrderMessageBO orderMessageBO = new OrderMessageBO(expiredKey);
        this.orderService.cancel(orderMessageBO.getOrderId());
        this.couponService.returnBack(orderMessageBO);
    }
}
