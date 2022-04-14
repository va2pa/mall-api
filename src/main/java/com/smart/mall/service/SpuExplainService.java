package com.smart.mall.service;

import com.smart.mall.model.SpuExplain;
import com.smart.mall.repository.SpuExplainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuExplainService {
    @Autowired
    private SpuExplainRepository spuExplainRepository;

    public List<SpuExplain> getSpuExplain(){
        return this.spuExplainRepository.findAll();
    }
}
