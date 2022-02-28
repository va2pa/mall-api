package com.smart.mall.web;

import com.smart.mall.model.Category;
import com.smart.mall.model.GridCategory;
import com.smart.mall.service.CategoryService;
import com.smart.mall.service.GridCategoryService;
import com.smart.mall.vo.CategoriesAllVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GridCategoryService gridCategoryService;

    @GetMapping("/all")
    public CategoriesAllVO getAll(){
        Map<Integer, List<Category>> map = this.categoryService.getAll();
        return new CategoriesAllVO(map);
    }

    @GetMapping("/grid")
    public List<GridCategory> getGridCategoryList(){
        return this.gridCategoryService.getGridCategoryList();
    }
}
