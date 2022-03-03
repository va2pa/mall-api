package com.smart.mall.exception;

import com.smart.mall.exception.http.HttpException;

public class DeleteSuccess extends HttpException {
    public DeleteSuccess(int code){
        this.code = code;
        this.httpStatusCode = 200;
    }
}
