package com.smart.mall.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class JwtDTO {
    @NotBlank(message = "code不允许为空")
    private String code;

    private String token;
}
