package com.smart.mall.exception.http;

public class UnAuthenticated extends HttpException {
    public UnAuthenticated(int code){
        this.code = code;
        this.httpStatusCode = 401;
    }
}
