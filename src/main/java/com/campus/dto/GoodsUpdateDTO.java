package com.campus.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsUpdateDTO {
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String category;
    private String cover;
}
