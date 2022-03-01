package com.smart.mall.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtToken {
    private static String jwtKey;
    private static Integer expiredTime;
    private static Integer defaultScope;

    @Value("${mall.security.jwt-key}")
    public void setJwtKey(String jwtKey){
        System.out.println(11);
        JwtToken.jwtKey = jwtKey;
    }
    @Value("${mall.security.token-expired-in}")
    public void setExpiredTime(Integer expiredTime){
        JwtToken.expiredTime = expiredTime;
    }

    public static String getToken(Long uid){
        return JwtToken.makeToken(uid, JwtToken.defaultScope);
    }

    public static String getToken(Long uid, Integer scope){
        return JwtToken.makeToken(uid, scope);
    }

    private static String makeToken(Long uid, Integer scope){
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        Map<String,Date> timeMap = JwtToken.calculateExpiredTime();
        String jwt = JWT.create()
                .withClaim("uid", uid)
                .withClaim("scope", scope)
                .withIssuedAt(timeMap.get("now"))
                .withExpiresAt(timeMap.get("expiredTime"))
                .sign(algorithm);
        return jwt;
    }
    private static Map<String,Date> calculateExpiredTime(){
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND,JwtToken.expiredTime);
        Map<String, Date> map = new HashMap<>();
        map.put("now",now);
        map.put("expiredTime",calendar.getTime());
        return map;
    }
}
