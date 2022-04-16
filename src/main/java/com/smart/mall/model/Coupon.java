package com.smart.mall.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Where(clause = "delete_time is null")
public class Coupon extends BaseEntity{
    @Id
    private Long id;
    private String title;
    private Date startTime;
    private Date endTime;
    private String description;
    private BigDecimal fullMoney;
    private BigDecimal minus;
    private BigDecimal rate;
    //1.满减劵 2.折扣劵 3.无门槛劵 4.满金额折扣劵
    private Integer type;
    private Long activityId;
    private String remark;
    private Boolean wholeStore;

    @ManyToMany(mappedBy = "couponList")
    private List<Category> categoryList;

}
