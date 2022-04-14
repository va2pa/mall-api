package com.smart.mall.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
public class SpuExplain {
    @Id
    private Long id;
    private String text;
    private Long spuId;
    private Long index;
}
