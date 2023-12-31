package com.smart.mall.web;

import com.smart.mall.bo.PageCounter;
import com.smart.mall.core.LocalUser;
import com.smart.mall.core.UnifyResponse;
import com.smart.mall.core.interceptors.ScopeLevel;
import com.smart.mall.exception.http.NotFoundException;
import com.smart.mall.model.Spu;
import com.smart.mall.model.SpuExplain;
import com.smart.mall.service.SpuExplainService;
import com.smart.mall.service.SpuService;
import com.smart.mall.util.CommonUtil;
import com.smart.mall.vo.PagingDozer;
import com.smart.mall.vo.SpuPureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smart.mall.core.enumeration.AccessLevel.LOGIN_USER;

@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {
    @Autowired
    private SpuService spuService;
    @Autowired
    private SpuExplainService spuExplainService;

    @GetMapping("/id/{id}/detail")
    public Spu getDetail(@PathVariable @Positive Long id){
        Spu spu = this.spuService.getSpuById(id);
        if(spu == null){
            throw new NotFoundException(4006);
        }
        return spu;
    }
    @GetMapping("/latest")
    public PagingDozer<Spu, SpuPureVO> getLatestSpuList(@RequestParam(defaultValue = "0") Integer start,
                                                        @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<Spu> page = this.spuService.getLatestPagingSpu(pageCounter.getPage(), pageCounter.getSize());
        return new PagingDozer<>(page, SpuPureVO.class);
    }

    @GetMapping("/by/category/{cid}")
    public PagingDozer<Spu, SpuPureVO> getByCategory(@PathVariable Long cid,
                                                        @RequestParam(name = "is_root", defaultValue = "false") Boolean isRoot,
                                                        @RequestParam(defaultValue = "0") Integer start,
                                                        @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<Spu> page = this.spuService.getByCategory(cid, isRoot, pageCounter.getPage(), pageCounter.getSize());
        return new PagingDozer<>(page, SpuPureVO.class);
    }

    @GetMapping("/search")
    public PagingDozer<Spu, SpuPureVO> search(@RequestParam String keyword,
                                                     @RequestParam(defaultValue = "0") Integer start,
                                                     @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<Spu> page = this.spuService.search(keyword, pageCounter.getPage(), pageCounter.getSize());
        return new PagingDozer<>(page, SpuPureVO.class);
    }

    @GetMapping("/explain")
    public List<SpuExplain> getSpuExplain(){
        return spuExplainService.getSpuExplain();
    }

    @ScopeLevel(LOGIN_USER)
    @PostMapping("/id/{id}/like")
    public void like(@PathVariable Long id){
        Long uid = LocalUser.getUser().getId();
        this.spuService.like(uid, id);
        UnifyResponse.updateSuccess();
    }

    @ScopeLevel(LOGIN_USER)
    @PostMapping("/id/{id}/disLike")
    public void disLike(@PathVariable Long id){
        Long uid = LocalUser.getUser().getId();
        this.spuService.disLike(uid, id);
        UnifyResponse.updateSuccess();
    }

    @ScopeLevel(LOGIN_USER)
    @GetMapping("/id/{id}/isLike")
    public Map<String, Boolean> isLike(@PathVariable Long id){
        Long uid = LocalUser.getUser().getId();
        boolean isLike = this.spuService.isLike(uid, id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("is_like", isLike);
        return map;
    }

    @ScopeLevel(LOGIN_USER)
    @GetMapping("/myself/favor")
    public PagingDozer<Spu, SpuPureVO> getMyFavSpuList(@RequestParam(defaultValue = "0") Integer start,
                                     @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);

        Long uid = LocalUser.getUser().getId();
        Page<Spu> page = this.spuService.getMyFavSpuList(uid, pageCounter.getPage(), pageCounter.getSize());
        return new PagingDozer<>(page, SpuPureVO.class);
    }
}
