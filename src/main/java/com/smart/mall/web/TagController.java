package com.smart.mall.web;

import com.smart.mall.model.Tag;
import com.smart.mall.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/all")
    public List<Tag> getAllTags(){
        return this.tagService.getAllTags();
    }
}
