package com.smart.mall.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.mall.dto.OrderAddressDTO;
import com.smart.mall.util.GenergicAndJson;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "delete_time is null")
@Table(name = "`Order`")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String orderNo;
    private Long userId;
    private Long totalCount;
    private BigDecimal totalPrice;
    private BigDecimal finalTotalPrice;
    private String snapImg;
    private String snapTitle;
    private String snapItems;
    private String snapAddress;
    private Integer status;
    private Date expiredTime;
    private Date placedTime;
    private String prepayId;

    public List<OrderSku> getSnapItems() {
        return GenergicAndJson.convertToObject(this.snapItems, new TypeReference<List<OrderSku>>() {
        });
    }

    public void setSnapItems(List<OrderSku> orderSkuList) {
        this.snapItems = GenergicAndJson.convertToJson(orderSkuList);
    }

    public OrderAddressDTO getSnapAddress() {
        if (this.snapAddress == null){
            return null;
        }
        return GenergicAndJson.convertToObject(this.snapAddress, new TypeReference<OrderAddressDTO>() {
        });
    }

    public void setSnapAddress(OrderAddressDTO orderAddressDTO) {
        this.snapAddress = GenergicAndJson.convertToJson(orderAddressDTO);
    }

}

