package com.smart.mall.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.mall.util.GenergicAndJson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Where(clause = "delete_time is null and online = 1")
public class Sku extends BaseEntity{
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
        this.specs = GenergicAndJson.convertToJson(specs);
    }

    public BigDecimal getActualPrice(){
        return this.discountPrice != null
                ? this.discountPrice : this.price;
    }

    @JsonIgnore
    public List<String> getSpecValueList() {
        if(this.getSpecs() == null){
            return Collections.emptyList();
        }
        return this.getSpecs().stream()
                .map(Spec::getValue)
                .collect(Collectors.toList());
    }
}
