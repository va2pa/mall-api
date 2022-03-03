package com.smart.mall.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long userId;
    private Long couponId;
    private Integer status;
    private Date createTime;
    private Long orderId;
}

