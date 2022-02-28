package com.smart.mall.service;

import com.smart.mall.model.Theme;
import com.smart.mall.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;

    public List<Theme> getTheListByNames(List<String> names){
        return this.themeRepository.findByNames(names);
    }

    public Optional<Theme> getThemeByNameWithSpu(String name){
        return this.themeRepository.findByName(name);
    }
}
