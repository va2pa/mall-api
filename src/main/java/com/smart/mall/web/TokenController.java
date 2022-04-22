package com.smart.mall.web;

import com.smart.mall.dto.JwtDTO;
import com.smart.mall.service.WxAuthenticationService;
import com.smart.mall.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/token")
@Validated
public class TokenController {
    @Autowired
    private WxAuthenticationService wxAuthenticationService;

    @PostMapping("/verify")
    public Map<String, Boolean> verify(@RequestBody JwtDTO jwtDTO){
        Map<String, Boolean> resMap = new HashMap<>();
        String token = jwtDTO.getToken();
        boolean valid = JwtUtils.verify(token);
        resMap.put("is_valid", valid);
        return resMap;
    }

    @PostMapping("/login")
    public Map<String, String> getToken(@RequestBody @Validated JwtDTO jwtDTO){
        Map<String, String> tokenMap = new HashMap<>();
        String code = jwtDTO.getCode();
        String token = wxAuthenticationService.registerOrLogin(code);
        tokenMap.put("token", token);
        return tokenMap;
    }
}
