package com.smart.mall.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    @Value("${wx.code2session}")
    private String code2SessionUrl;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;

    public String registerOrLogin(String code){
        Map<String, Object> session = code2session(code);
        String openid = (String)session.get("openid");
        if(openid == null){
            throw new ParameterException(5004);
        }
        User user = this.userRepository.findByOpenid(openid);
        if(user == null){
            //register
            user = User.builder()
                    .openid(openid)
                    .build();
            this.userRepository.save(user);

        }
        String token = JwtToken.getToken(user.getId());
        return token;
    }

    /**
     * 利用code请求微信服务器获取包含openid的sessionMap
     * @param code
     * @return sessionMap
     */
    private Map<String, Object> code2session(String code){
        String url = MessageFormat.format(this.code2SessionUrl, this.appid, this.appsecret, code);
        RestTemplate rest = new RestTemplate();
        String sessionText = rest.getForObject(url, String.class);
        Map<String, Object> session = new HashMap<>();
        try {
            session = mapper.readValue(sessionText, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return session;
    }


}
