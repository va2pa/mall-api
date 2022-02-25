package com.smart.mall.service;

import com.smart.mall.model.Spu;
import com.smart.mall.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuService {
    @Autowired
    private SpuRepository spuRepository;

    public Spu getSpuById(Long id){
        return this.spuRepository.findOneById(id);
    }
}
