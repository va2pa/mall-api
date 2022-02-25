package com.smart.mall.service;

import com.smart.mall.model.Banner;
import com.smart.mall.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerService {
    @Autowired
    private BannerRepository bannerRepository;

    public Banner getByName(String name){
        Banner banner = this.bannerRepository.findOneByName(name);
        return banner;
    }
}
