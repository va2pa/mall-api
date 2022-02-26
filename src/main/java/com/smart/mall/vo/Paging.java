package com.smart.mall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Paging<T> {
    private Integer page;
    private Integer count;
    private Long total;
    private Integer totalPage;
    private List<T> items;

    public Paging(Page<T> pageT){
        this.initPageParameters(pageT);
        this.items = pageT.getContent();
    }

    protected void initPageParameters(Page<T> pageT){
        this.page = pageT.getNumber();
        this.count = pageT.getSize();
        this.total = pageT.getTotalElements();
        this.totalPage = pageT.getTotalPages();
    }
}
