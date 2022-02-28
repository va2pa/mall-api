package com.smart.mall.model;

import com.smart.mall.util.ListAndJson;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Sku {
    @Id
    private Long id;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Boolean online;
    private String img;
    private String title;
    private Long spuId;
    @Convert(converter = ListAndJson.class)
    private List<Object> specs;
    private String code;
    private Long stock;
    private Long categoryId;
    private Long rootCategoryId;

}
