package com.smart.mall.core.configuration;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "mall")
@PropertySource("classpath:config/response-code.properties")
@Setter
@Component
public class ResponseCodeConfiguration {
    private Map<Integer, String> codes = new HashMap<>();

    public String getMessage(int code){
        return codes.get(code);
    }

}
