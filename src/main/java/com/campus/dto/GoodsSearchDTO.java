package com.campus.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsSearchDTO {
    private String keyword;
    private String category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sortBy;// 排序字段
    private String order;// 排序方式 asc desc
    private Integer current = 1;//当前页
    private Integer size = 10;//每页数量
}
