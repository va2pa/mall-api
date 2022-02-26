package com.smart.mall.util;

import com.smart.mall.bo.PageCounter;

public class CommonUtil {
    public static PageCounter convertToPageParameter(Integer start, Integer count){
        return PageCounter.builder()
                .page(start / count)
                .size(count)
                .build();
    }
}
