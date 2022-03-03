package com.smart.mall.exception;

import com.smart.mall.exception.http.HttpException;

public class CreateSuccess extends HttpException {
    public CreateSuccess(int code){
        this.code = code;
        this.httpStatusCode = 201;
    }
}
