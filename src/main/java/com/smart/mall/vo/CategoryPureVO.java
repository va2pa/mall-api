package com.smart.mall.vo;

import com.smart.mall.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Id;

@Getter
@Setter
public class CategoryPureVO {
    @Id
    private Long id;
    private String name;
    private Boolean isRoot;
    private Long parentId;
    private String img;
    private Long index;

    public CategoryPureVO(Category category){
        BeanUtils.copyProperties(category, this);
    }
}
