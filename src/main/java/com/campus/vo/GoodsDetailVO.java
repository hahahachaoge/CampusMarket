package com.campus.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    @Schema(description = "商品图片列表")
    private List<String> images;
}
