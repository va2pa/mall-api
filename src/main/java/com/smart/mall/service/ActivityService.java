package com.smart.mall.service;

import com.smart.mall.model.Activity;
import com.smart.mall.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityService{
    @Autowired
    private ActivityRepository activityRepository;

    public Optional<Activity> getByName(String name){
        return this.activityRepository.findByName(name);
    }
}
