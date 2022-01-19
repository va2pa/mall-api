package com.smart.mall.exception.http;

import lombok.Getter;

@Getter
public class HttpException extends RuntimeException{
    protected int code;
    protected int httpStatusCode = 500;
}
