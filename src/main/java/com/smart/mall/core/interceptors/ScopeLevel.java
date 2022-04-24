package com.smart.mall.core.interceptors;

import java.lang.annotation.*;

import static com.smart.mall.core.enumeration.AccessLevel.LOGIN_USER;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ScopeLevel {
    int value() default LOGIN_USER;
}