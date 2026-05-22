package com.campus.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GoodsVO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String category;
    private String cover;
    private Integer status;
    private String statusText;
    private Integer viewCount;
    private LocalDateTime createTime;
}
