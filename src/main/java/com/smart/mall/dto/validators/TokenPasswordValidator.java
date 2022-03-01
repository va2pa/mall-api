package com.smart.mall.dto.validators;

import com.smart.mall.dto.TokenGetDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TokenPasswordValidator implements ConstraintValidator<TokenPassword, String> {
    private Integer min;
    private Integer max;

    @Override
    public void initialize(TokenPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasLength(s)){
            //如果前端是小程序该值为空
            return true;
        }
        //其他前端
        return s.length() >= this.min && s.length() <= this.max;
    }
}
