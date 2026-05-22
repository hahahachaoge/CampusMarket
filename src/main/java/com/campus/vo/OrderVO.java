package com.campus.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVO {
    private Long id;
    private String orderNo;
    private BigDecimal price;
    private Integer status;
    private String statusText;
    private LocalDateTime createTime;
    //商品信息
    private Long goodsId;
    private String goodsTitle;
    private String goodsCover;
    //卖家信息
    private Long sellerId;
    private String sellerNickname;
    //买家信息
    private Long buyerId;
    private String buyerNickname;
}
