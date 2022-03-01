package com.smart.mall.core.interceptors;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ScopeLevel {
    int value() default 3;
}