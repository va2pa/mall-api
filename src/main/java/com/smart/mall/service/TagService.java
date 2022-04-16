package com.smart.mall.service;

import com.smart.mall.model.SpuExplain;
import com.smart.mall.model.Tag;
import com.smart.mall.repository.SpuExplainRepository;
import com.smart.mall.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTags(){
        return this.tagRepository.findAll();
    }
}
