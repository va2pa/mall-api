package com.smart.mall.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@Entity
@Where(clause = "delete_time is null")
public class Tag extends BaseEntity{
    @Id
    private Long id;
    private String title;
    private String description;
    private Integer highlight;
    private Integer type;
}
