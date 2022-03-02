package com.smart.mall.service;

import com.smart.mall.model.User;
import com.smart.mall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long id){
        return this.userRepository.findFirstById(id);
    }
}
