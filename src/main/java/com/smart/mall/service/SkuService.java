package com.smart.mall.service;

import com.smart.mall.model.Sku;
import com.smart.mall.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuService {
    @Autowired
    private SkuRepository skuRepository;

    public List<Sku> getSkuListByIds(List<Long> ids){
        return this.skuRepository.findAllByIdInOrderById(ids);
    }
}
