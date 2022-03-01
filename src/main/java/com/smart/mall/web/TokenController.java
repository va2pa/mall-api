package com.smart.mall.web;

import com.smart.mall.dto.TokenDTO;
import com.smart.mall.dto.TokenGetDTO;
import com.smart.mall.exception.http.NotFoundException;
import com.smart.mall.service.WxAuthenticationService;
import com.smart.mall.util.JwtToken;
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

    @PostMapping("/login")
    public Map<String, String> getToken(@RequestBody @Validated TokenGetDTO userData){
        Map<String, String> map = new HashMap<>();
        String token = null;
        switch (userData.getLoginType()){
            case USER_WX:
                token = wxAuthenticationService.registerOrLogin(userData.getAccount());
                break;
            case USER_EMAIL:
                break;
            default:
                throw new NotFoundException(4010);
        }
        map.put("token", token);
        return map;
    }

    @PostMapping("/verify")
    public Map<String, Boolean> verify(@RequestBody TokenDTO tokenDTO){
        String token = tokenDTO.getToken();
        boolean valid = JwtToken.verify(token);
        Map<String, Boolean> map = new HashMap<>();
        map.put("is_valid", valid);
        return map;
    }
}
