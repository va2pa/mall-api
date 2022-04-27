package com.smart.mall.web;

import com.smart.mall.core.LocalUser;
import com.smart.mall.core.UnifyResponse;
import com.smart.mall.core.interceptors.ScopeLevel;
import com.smart.mall.model.Spu;
import com.smart.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smart.mall.core.enumeration.AccessLevel.LOGIN_USER;
import static com.smart.mall.core.enumeration.AccessLevel.VIP;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ScopeLevel(LOGIN_USER)
    @PostMapping("/vip/register")
    public void registerVip(){
        Long uid = LocalUser.getUser().getId();
        this.userService.registerVip(uid);
        UnifyResponse.updateSuccess();
    }

    @ScopeLevel(VIP)
    @GetMapping("/vip/check")
    public Map<String,Boolean> checkVip(){
        // 权限不足会在拦截器抛出ForbiddenException
        Map<String,Boolean> map = new HashMap<>();
        map.put("is_vip", true);
        return map;
    }
}
