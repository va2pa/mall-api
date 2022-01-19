package com.smart.mall.core;

public class UnifyResponse{
    private int code;
    private String message;
    private String requestURL;

    public UnifyResponse(int code, String message, String requestURL) {
        this.code = code;
        this.message = message;
        this.requestURL = requestURL;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getrequestURL() {
        return requestURL;
    }
}
