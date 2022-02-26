package com.smart.mall.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.smart.mall.model.Spu;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PagingDozer<T, K> extends Paging {
    @JsonIgnore
    private Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public PagingDozer(Page<T> pageT, Class<K> clazz){
        initPageParameters(pageT);

        List<T> list = pageT.getContent();
        List<K> voList = new ArrayList<>(list.size());
        list.forEach(t -> {
            K vo = mapper.map(t, clazz);
            voList.add(vo);
        });
        this.setItems(voList);
    }
}
