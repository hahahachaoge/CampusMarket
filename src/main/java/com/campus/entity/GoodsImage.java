package com.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GoodsImage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long goodsId;
    private String imageUrl;
    private LocalDateTime createTime;
}
