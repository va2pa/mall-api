package com.smart.mall.service;

import com.smart.mall.model.Category;
import com.smart.mall.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Map<Integer, List<Category>> getAll(){
        List<Category> roots = this.categoryRepository.findAllByIsRootOrderByIndexAsc(true);
        List<Category> subs = this.categoryRepository.findAllByIsRootOrderByIndexAsc(false);
        Map<Integer, List<Category>> map = new HashMap<>();
        map.put(1, roots);
        map.put(2, subs);
        return map;
    }
}
