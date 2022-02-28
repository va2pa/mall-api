package com.smart.mall.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Setter
@Getter
@Where(clause = "delete_time is null")
public class Spec {
    private Long keyId;
    private String key;
    private Long valueId;
    private String value;
}
