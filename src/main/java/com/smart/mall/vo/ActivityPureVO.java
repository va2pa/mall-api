package com.smart.mall.vo;

import com.smart.mall.model.Activity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ActivityPureVO {
    private Long id;
    private String title;
    private Date startTime;
    private Date endTime;
    private String remark;
    private Boolean online;
    private String entranceImg;


    public ActivityPureVO(Activity activity){
        BeanUtils.copyProperties(activity, this);
    }
    public ActivityPureVO(Object activity){
        BeanUtils.copyProperties(activity, this);
    }
}
