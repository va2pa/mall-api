package com.smart.mall.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@Entity
public class SpuDetailImg {
    @Id
    private Long id;
    private String img;
    private Long spuId;
    private Long index;

}
