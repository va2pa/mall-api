package com.smart.mall.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtUtils {
    private static String secretKey;
    private static Integer defaultScope = 7;
    private static Integer expiredTime;

    @Value("${mall.security.secret-key}")
    public void setSecretKey(String secretKey){
        JwtUtils.secretKey = secretKey;
    }
    @Value("${mall.security.token-expired-time}")
    public void setExpiredTime(Integer expiredTime){
        JwtUtils.expiredTime = expiredTime;
    }

    public static String getToken(Long uid, Integer scope){
        return JwtUtils.makeToken(uid, scope);
    }

    public static String getToken(Long uid){
        return JwtUtils.makeToken(uid, JwtUtils.defaultScope);
    }

    private static String makeToken(Long uid, Integer scope){
        Algorithm algorithm = Algorithm.HMAC256(JwtUtils.secretKey);
        // 根据时间差计算过期时间
        Map<String,Date> timeMap = JwtUtils.getExpiredTime();
        String jwt = JWT.create()
                .withClaim("scope", scope)
                .withIssuedAt(timeMap.get("now"))
                .withExpiresAt(timeMap.get("expiredTime"))
                .withClaim("uid", uid)
                .sign(algorithm);
        return jwt;
    }

    public static Optional<Map<String, Claim>> verifyAndGetClaims(String token){
        Algorithm algorithm = Algorithm.HMAC256(JwtUtils.secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT;
        try{
            // 对令牌解码顺便验证令牌是否合法
            decodedJWT = jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            // 解码错误（令牌验证失败）
            return Optional.empty();
        }
        // 返回令牌存储的信息
        return Optional.of(decodedJWT.getClaims());
    }

    public static boolean verify(String token){
        Algorithm algorithm = Algorithm.HMAC256(JwtUtils.secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            // 令牌验证失败
            return false;
        }
        return true;
    }

    private static Map<String,Date> getExpiredTime(){
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND, JwtUtils.expiredTime);
        Map<String, Date> timeMap = new HashMap<>();
        timeMap.put("expiredTime",calendar.getTime());
        timeMap.put("now",now);
        return timeMap;
    }
}
