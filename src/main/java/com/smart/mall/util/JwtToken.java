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
public class JwtToken {
    private static String jwtKey;
    private static Integer expiredTime;
    private static Integer defaultScope = 6;

    @Value("${mall.security.jwt-key}")
    public void setJwtKey(String jwtKey){
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

    public static Optional<Map<String, Claim>> verifyAndGetClaims(String token){
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT;
        try{
            decodedJWT = jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            //verification failed
            return Optional.empty();
        }
        return Optional.of(decodedJWT.getClaims());
    }

    public static boolean verify(String token){
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            //verification failed
            return false;
        }
        return true;
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
