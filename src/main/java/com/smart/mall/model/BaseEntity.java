package com.smart.mall.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass   //代替@Entity，表明它是映射类的基类，字段才能映射
public abstract class BaseEntity {
    @JsonIgnore
    @Column(insertable=false, updatable=false)
    private Date createTime;
    @JsonIgnore
    @Column(insertable=false, updatable=false)
    private Date updateTime;
    @JsonIgnore
    private Date deleteTime;
}
