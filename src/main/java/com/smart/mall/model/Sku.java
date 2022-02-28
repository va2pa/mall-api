package com.smart.mall.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.mall.util.GenergicAndJson;
import com.smart.mall.util.ListAndJson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Where(clause = "delete_time is null")
public class Sku {
    @Id
    private Long id;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Boolean online;
    private String img;
    private String title;
    private Long spuId;
    private String specs;
    private String code;
    private Long stock;
    private Long categoryId;
    private Long rootCategoryId;

    public List<Spec> getSpecs(){
        return GenergicAndJson.convertToObject(this.specs, new TypeReference<List<Spec>>() {
        });
    }

    public void setSpecs(List<Spec> specs){
        this.specs = GenergicAndJson.convertToString(specs);
    }
}
