package com.smart.mall.core;

import com.smart.mall.core.configuration.ResponseCodeConfiguration;
import com.smart.mall.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @Autowired
    private ResponseCodeConfiguration responseCodeConfiguration;

    /**
     * 统一处理未知异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifyResponse handleException(HttpServletRequest req, Exception e){
        //打印异常，开发阶段方便调试
        e.printStackTrace();

        int code = 9999;
        String message = responseCodeConfiguration.getMessage(9999);
        String RequestURL = req.getMethod() + " " + req.getRequestURI();
        return new UnifyResponse(code, message, RequestURL);
    }

    /**
     * 处理Http异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public ResponseEntity<UnifyResponse> handleHttpException(HttpServletRequest req, HttpException e){
        //动态设置HttpStatus
        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());

        String message = responseCodeConfiguration.getMessage(e.getCode());
        System.out.println(message);
        String RequestURL = req.getMethod() + " " + req.getRequestURI();
        UnifyResponse resp = new UnifyResponse(e.getCode(), message, RequestURL);
        return new ResponseEntity<UnifyResponse>(resp, httpStatus);
    }
}
