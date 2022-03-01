package com.smart.mall.dto;

import com.smart.mall.core.enumeration.LoginType;
import com.smart.mall.dto.validators.TokenPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TokenGetDTO {
    @NotBlank(message = "account不允许为空")
    private String account;
    @TokenPassword(message = "{token.password}")
    private String password;

    private LoginType loginType;
}
