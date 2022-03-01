package com.smart.mall.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = TokenPasswordValidator.class)
public @interface TokenPassword {
    String message() default "字段不符合要求";

    int min() default 6;

    int max() default 24;

    //校验注解规范方法
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};
}
