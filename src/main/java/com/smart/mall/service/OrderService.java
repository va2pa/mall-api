package com.smart.mall.service;

import com.smart.mall.core.enumeration.OrderStatus;
import com.smart.mall.core.money.MoneyDiscount;
import com.smart.mall.dto.OrderDTO;
import com.smart.mall.dto.SkuInfoDTO;
import com.smart.mall.exception.http.ForbiddenException;
import com.smart.mall.exception.http.NotFoundException;
import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.exception.http.ServerErrorException;
import com.smart.mall.logic.CouponCheck;
import com.smart.mall.logic.OrderCheck;
import com.smart.mall.model.*;
import com.smart.mall.repository.CouponRepository;
import com.smart.mall.repository.OrderRepository;
import com.smart.mall.repository.SkuRepository;
import com.smart.mall.repository.UserCouponRepository;
import com.smart.mall.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.smart.mall.core.enumeration.OrderStatus.PAID;

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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 1. 设置订单状态为已取消（可选）
     * 2. 归还该订单sku库存
     * @param oid
     */
    @Transactional
    public void cancel(long oid) {
        if(oid <= 0){
            throw new ServerErrorException(4016);
        }
        Order order = orderRepository.findById(oid)
                .orElseThrow(() -> new ServerErrorException(4016));
        int res = orderRepository.cancelOrder(oid);
        if(res != 1){
            return;
        }
        order.getSnapItems().forEach(s -> {
            skuRepository.recoverStock(s.getId(),s.getCount().longValue());
        });
    }

    public Optional<Order> getDetail(Long oid, Long uid) {
        return this.orderRepository.findFirstByIdAndUserId(oid, uid);
    }

    public Page<Order> getByStatus(Integer status, Integer page, Integer size, Long uid){
        if (status == OrderStatus.UNPAID.getValue() ||status == OrderStatus.CANCELED.getValue()){
            throw new ParameterException(7010);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        if (status == OrderStatus.All.getValue()){
            return orderRepository.findByUserId(uid, pageable);
        }
        return orderRepository.findByStatusAndUserId(status, uid, pageable);
    }

    public Page<Order> getUnpaid(Integer page, Integer size, Long uid){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return orderRepository.findByStatusAndExpiredTimeGreaterThanAndUserId(
                OrderStatus.UNPAID.getValue(),new Date(), uid, pageable);
    }

    @Transactional
    public Long placeOrder(long uid, OrderDTO orderDTO, OrderCheck orderCheck) {
        // 此时传入的orderDTO已通过校验
        Calendar now = Calendar.getInstance();
        Calendar expireTime = (Calendar)now.clone();
        expireTime.add(Calendar.SECOND, this.payTimeLimit);
        Order order = Order.builder()
                .orderNo(OrderUtil.makeOrderNo())
                .userId(uid)
                .totalPrice(orderDTO.getTotalPrice())
                .finalTotalPrice(orderDTO.getFinalTotalPrice())
                .totalCount(orderCheck.getTotalCount().longValue())
                .snapImg(orderCheck.getSnapImg())
                .snapTitle(orderCheck.getSnapTitle())
                .status(OrderStatus.UNPAID.getValue())
                .placedTime(now.getTime())
                .expiredTime(expireTime.getTime())
                .build();
        order.setSnapAddress(orderDTO.getAddress());
        order.setSnapItems(orderCheck.getOrderSkuList());
        orderRepository.save(order);
        reduceStock(orderCheck.getOrderSkuList());
        long couponId = -1;
        if (orderDTO.getCouponId() != null){
            writeOffCoupon(orderDTO.getCouponId(), uid, order.getId());
            couponId = orderDTO.getCouponId();
        }
//        sendToRedis(order.getId(), uid, couponId);
        return order.getId();
    }

    private void sendToRedis(long oid, long uid, long couponId){
        String key = oid + "," + uid + "," + couponId;
        try{
            stringRedisTemplate.opsForValue().set(key,"",this.payTimeLimit, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
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

    public void fakePay(Long oid) {
        this.orderRepository.updateOrderStatus(oid, PAID.getValue());
    }
}
