package com.smart.mall.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@Entity
@Where(clause = "delete_time is null")
public class SpuImg extends BaseEntity{
    @Id
    private Long id;
    private String img;
    private Long spuId;

}
