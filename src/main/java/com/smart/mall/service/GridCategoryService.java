package com.smart.mall.service;

import com.smart.mall.model.GridCategory;
import com.smart.mall.repository.GridCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GridCategoryService {
    @Autowired
    private GridCategoryRepository gridCategoryRepository;

    public List<GridCategory> getGridCategoryList(){
        return this.gridCategoryRepository.findAll();
    }
}
