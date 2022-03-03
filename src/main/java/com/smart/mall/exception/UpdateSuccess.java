package com.smart.mall.exception;

import com.smart.mall.exception.http.HttpException;

public class UpdateSuccess extends HttpException {
    public UpdateSuccess(int code){
        this.code = code;
        this.httpStatusCode = 200;
    }
}
