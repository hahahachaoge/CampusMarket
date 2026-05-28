package com.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.entity.GoodsImage;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsImageMapper extends BaseMapper<GoodsImage> {
    //根据商品ID查询图片
    List<GoodsImage> selectByGoodsId(@Param("goodsId") Long goodsId);
    //根据商品ID删除照片
    @Delete("delete from goods_image where goods_id = #{goodsId}")
    void deleteByGoodsId(@Param("goodsId") Long goodsId);
}
