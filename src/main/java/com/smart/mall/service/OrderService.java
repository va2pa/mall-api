package com.smart.mall.service;

import com.smart.mall.core.enumeration.OrderStatus;
import com.smart.mall.core.money.MoneyDiscount;
import com.smart.mall.dto.OrderDTO;
import com.smart.mall.dto.SkuInfoDTO;
import com.smart.mall.exception.http.ForbiddenException;
import com.smart.mall.exception.http.NotFoundException;
import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.logic.CouponCheck;
import com.smart.mall.logic.OrderCheck;
import com.smart.mall.model.*;
import com.smart.mall.repository.CouponRepository;
import com.smart.mall.repository.OrderRepository;
import com.smart.mall.repository.SkuRepository;
import com.smart.mall.repository.UserCouponRepository;
import com.smart.mall.util.OrderUtil;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private SkuRepository skuRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private MoneyDiscount moneyDiscount;
    @Autowired
    private OrderRepository orderRepository;
    @Value("${mall.order.max-sku-limit}")
    private Integer maxSkuLimit;
    @Value("${mall.order.pay-time-limit}")
    private Integer payTimeLimit;

    public Optional<Order> getDetail(Long oid, Long uid) {
        return this.orderRepository.findFirstByIdAndUserId(oid, uid);
    }

    public Page<Order> getByStatus(Integer status, Integer page, Integer size, Long uid){
        if (status == OrderStatus.UNPAID.value() ||status == OrderStatus.CANCELED.value()){
            throw new ParameterException(7010);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        if (status == OrderStatus.All.value()){
            return orderRepository.findByUserId(uid, pageable);
        }
        return orderRepository.findByStatusAndUserId(status, uid, pageable);
    }

    public Page<Order> getUnpaid(Integer page, Integer size, Long uid){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return orderRepository.findByStatusAndExpiredTimeGreaterThanAndUserId(
                OrderStatus.UNPAID.value(),new Date(), uid, pageable);
    }

    @Transactional
    public Long placeOrder(long uid, OrderDTO orderDTO, OrderCheck orderCheck) {
        Calendar now = Calendar.getInstance();
        Calendar expireTime = (Calendar)now.clone();
        expireTime.add(Calendar.SECOND, this.payTimeLimit);
        Order order = Order.builder()
                .orderNo(OrderUtil.makeOrderNo())
                .userId(uid)
                .totalPrice(orderDTO.getTotalPrice())
                .totalCount(orderCheck.getTotalCount().longValue())
                .snapImg(orderCheck.getSnapImg())
                .snapTitle(orderCheck.getSnapTitle())
                .status(OrderStatus.UNPAID.value())
                .placedTime(now.getTime())
                .expiredTime(expireTime.getTime())
                .build();
        order.setSnapAddress(orderDTO.getAddress());
        order.setSnapItems(orderCheck.getOrderSkuList());
        orderRepository.save(order);
        reduceStock(orderCheck.getOrderSkuList());
        if (orderDTO.getCouponId() != null){
            writeOffCoupon(orderDTO.getCouponId(), uid, order.getId());
        }
        return order.getId();
    }

    private void writeOffCoupon(Long couponId, long uid, Long orderId) {
        int result = userCouponRepository.writeOffCoupon(couponId, uid, orderId, new Date());
        if (result != 1){
            throw new ForbiddenException(6018);
        }
    }

    private void reduceStock(List<OrderSku> orderSkuList) {
        for (OrderSku orderSku : orderSkuList) {
            int result = this.skuRepository.reduceStock(orderSku.getId(), orderSku.getCount().longValue());
            if (result != 1){
                throw new ParameterException(7512);
            }
        }
    }

    public OrderCheck isOk(Long uid, OrderDTO orderDTO){
        List<Long> skuIdList = orderDTO.getSkuInfoList().stream()
                .map(SkuInfoDTO::getId)
                .collect(Collectors.toList());
        List<Sku> skuList = skuRepository.findAllByIdInOrderById(skuIdList);
        Long couponId = orderDTO.getCouponId();
        //该笔订单是否使用了优惠劵，如果有，则校验优惠劵
        CouponCheck couponCheck = null;
        if(couponId != null){
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new NotFoundException(6004));
            List<Coupon> myAvailable = couponRepository.findMyAvailable(uid, new Date());
            if (!myAvailable.contains(coupon)){
                throw new ParameterException(6016);
            }
            couponCheck = new CouponCheck(coupon, moneyDiscount);
        }
        OrderCheck orderCheck = new OrderCheck(
                orderDTO, skuList, couponCheck,  this.maxSkuLimit);
        orderCheck.isOk();
        return orderCheck;
    }



}
