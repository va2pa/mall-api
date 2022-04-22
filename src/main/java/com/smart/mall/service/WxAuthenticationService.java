package com.smart.mall.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.model.User;
import com.smart.mall.repository.UserRepository;
import com.smart.mall.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxAuthenticationService {
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.appsecret}")
    private String appsecret;
    @Value("${wx.info-url}")
    private String infoUrl;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    public String registerOrLogin(String code){
        // 使用code请求微信服务器换取openid（用户小程序唯一标识）
        String openid = getOpenidByCode(code);
        if(openid == null){
            throw new ParameterException(5004);
        }
        // 使用openid查询用户表中的用户
        User user = this.userRepository.findByOpenid(openid);
        if(user == null){
            // 如果用户表中不存在该openid对应的用户记录，就初始化user对象，并插入用户表中（注册）
            user = User.builder()
                    .openid(openid)
                    .build();
            this.userRepository.save(user);
        }
        // 如果数据表中已存在该用户，就返回令牌（登录）
        String token = JwtToken.getToken(user.getId());
        return token;
    }

    private String getOpenidByCode(String code){
        // 拼接url
        String requestUrl = MessageFormat.format(this.infoUrl, this.appid, this.appsecret, code);
        // 通过url发送请求获取包含openid的用户信息字符串
        String infoStr = new RestTemplate().getForObject(requestUrl, String.class);
        Map<String, Object> infoMap = new HashMap<>();
        try {
            //字符串转Map
            infoMap = objectMapper.readValue(infoStr, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回Map中的openid项
        return (String)infoMap.get("openid");
    }


}
