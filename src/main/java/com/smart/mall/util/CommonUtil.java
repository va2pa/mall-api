package com.smart.mall.util;

import com.smart.mall.bo.PageCounter;

import java.util.Date;

public class CommonUtil {
    public static PageCounter convertToPageParameter(Integer start, Integer count){
        return PageCounter.builder()
                .page(start / count)
                .size(count)
                .build();
    }

    public static boolean isInTimeLine(Date now, Date start, Date end){
        long nowTime = now.getTime();
        long startTime = start.getTime();
        long endTime = end.getTime();
        if(nowTime >= startTime && nowTime <= endTime){
            return true;
        }
        return false;
    }
}
