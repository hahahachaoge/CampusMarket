package com.campus.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GoodsDetailVO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String category;
    private String cover;
    private Integer viewCount;
    private Integer status;
    private String statusText;
    private LocalDateTime createTime;
    //发布人信息
    private Long userId;
    private String nickname;
    private String avatar;
}
