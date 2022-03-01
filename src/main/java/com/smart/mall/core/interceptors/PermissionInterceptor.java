package com.smart.mall.core.interceptors;

import com.auth0.jwt.interfaces.Claim;
import com.smart.mall.exception.http.ForbiddenException;
import com.smart.mall.exception.http.UnAuthenticated;
import com.smart.mall.util.JwtToken;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class PermissionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ScopeLevel scopeLevel = getScopeLevel(handler);
        if (scopeLevel == null){
            //open api
            return true;
        }
        //校验token是否合法(包括过期)
        String bearerToken = request.getHeader("Authorization");
        if (!StringUtils.hasLength(bearerToken) || !bearerToken.startsWith("Bearer")){
            throw new UnAuthenticated(1004);
        }
        String token = bearerToken.split(" ")[1];
        Map<String, Claim> claimMap = JwtToken.verifyAndGetClaims(token)
                .orElseThrow(() -> new UnAuthenticated(1004));
        //校验token权限是否大于api访问权限
        checkPermission(scopeLevel, claimMap);
        return true;
    }

    private void checkPermission(ScopeLevel scopeLevel, Map<String, Claim> claimMap){
        Integer scope = claimMap.get("scope").asInt();
        if (scope < scopeLevel.value()){
            throw new ForbiddenException(1005);
        }
    }

    private ScopeLevel getScopeLevel(Object handler) {
        if (handler instanceof HandlerMethod){
            return ((HandlerMethod) handler).getMethod()
                    .getAnnotation(ScopeLevel.class);
        }
        return null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
