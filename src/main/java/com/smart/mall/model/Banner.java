package com.smart.mall.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Banner{
    @Id
    private Long id;
    private String name;
    private String description;
    private String title;
    private String img;

}
