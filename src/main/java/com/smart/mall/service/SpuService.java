package com.smart.mall.service;

import com.smart.mall.exception.http.ParameterException;
import com.smart.mall.model.Spu;
import com.smart.mall.model.UserSpu;
import com.smart.mall.repository.SpuRepository;
import com.smart.mall.repository.UserSpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpuService {
    @Autowired
    private SpuRepository spuRepository;
    @Autowired
    private UserSpuRepository userSpuRepository;

    public Spu getSpuById(Long id){
        return this.spuRepository.findOneById(id);
    }

    public Page<Spu> getLatestPagingSpu(Integer pageNum, Integer size){
        Pageable page = PageRequest.of(pageNum, size, Sort.by("createTime").descending());
        return this.spuRepository.findAll(page);
    }

    public Page<Spu> getByCategory(Long cid, Boolean isRoot, Integer pageNum, Integer size){
        Pageable page = PageRequest.of(pageNum, size);
        if (isRoot){
            return this.spuRepository.findByRootCategoryId(cid, page);
        }else{
            return this.spuRepository.findByCategoryId(cid, page);
        }
    }

    public Page<Spu> search(String keyword, Integer pageNum, Integer size) {
        Pageable page = PageRequest.of(pageNum, size);
        return this.spuRepository.findByTitleLike("%" + keyword + "%", page);
    }

    @Transactional
    public void like(Long uid, Long id) {
        UserSpu existed = this.userSpuRepository.findFirstByUserIdAndSpuId(uid, id);
        if(existed != null){
            throw new ParameterException(8051);
        }
        UserSpu userSpu = UserSpu.builder()
                .userId(uid)
                .spuId(id)
                .build();
        this.userSpuRepository.save(userSpu);
        int res = this.spuRepository.increaseFavorNum(id);
        if(res != 1){
            throw new ParameterException(4006);
        }
    }

    @Transactional
    public void disLike(Long uid, Long id) {
        UserSpu existed = this.userSpuRepository.findFirstByUserIdAndSpuId(uid, id);
        if(existed == null){
            throw new ParameterException(8052);
        }
        this.userSpuRepository.deleteById(existed.getId());
        int res = this.spuRepository.decreaseFavorNum(id);
        if(res != 1){
            throw new ParameterException(4006);
        }
    }

    public boolean isLike(Long uid, Long id){
        UserSpu userSpu = this.userSpuRepository.findFirstByUserIdAndSpuId(uid, id);
        return userSpu != null;
    }
}
