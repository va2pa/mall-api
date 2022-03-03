package com.smart.mall.core;

import com.smart.mall.exception.CreateSuccess;
import com.smart.mall.exception.DeleteSuccess;
import com.smart.mall.exception.UpdateSuccess;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnifyResponse{
    private int code;
    private String message;
    private String requestURL;

    public static CreateSuccess createSuccess(){
        throw new CreateSuccess(0);
    }
    public static DeleteSuccess deleteSuccess(){
        throw new DeleteSuccess(0);
    }
    public static UpdateSuccess updateSuccess(){
        throw new UpdateSuccess(0);
    }
}
