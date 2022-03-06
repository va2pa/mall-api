package com.smart.mall.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.mall.exception.http.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GenergicAndJson {
    private static ObjectMapper mapper;
    @Autowired
    public void setMapper(ObjectMapper mapper){
        GenergicAndJson.mapper = mapper;
    }

    public static <T> String convertToJson(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    /**
     * 引入TypeReference解决了参数为List<Spec>无法传入其Class对象
     * @param s
     * @param tr
     * @param <T>
     * @return
     */
    public static <T> T convertToObject(String s, TypeReference<T> tr) {
        if (s == null){
            return null;
        }
        try {
            return mapper.readValue(s, tr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }
}
