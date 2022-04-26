package com.smart.mall.service;

import com.smart.mall.core.enumeration.AccessLevel;
import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.model.Order;
import com.smart.mall.model.User;
import com.smart.mall.repository.OrderRepository;
import com.smart.mall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Value("${mall.user.vip-threshold}")
    private Long vipThreshold;

    public User getUserById(Long id){
        return this.userRepository.findFirstById(id);
    }

    public void registerVip(Long uid){
        User user = this.userRepository.findFirstByIdAndLevel(uid, AccessLevel.VIP);
        if(user != null){
            return;
        }
        Order order = this.orderRepository.findFirstFinalTotalPriceGreaterThan(uid, vipThreshold);
        if(order == null){
            throw new ParameterException(8002);
        }
        this.userRepository.updateUserLevel(uid, AccessLevel.VIP);
    }
}
