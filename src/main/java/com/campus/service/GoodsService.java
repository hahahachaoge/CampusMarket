package com.campus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.dto.GoodsPublishDTO;
import com.campus.dto.GoodsSearchDTO;
import com.campus.dto.GoodsUpdateDTO;
import com.campus.entity.Goods;
import com.campus.vo.GoodsDetailVO;
import com.campus.vo.GoodsVO;

public interface GoodsService extends IService<Goods> {
    void publish(GoodsPublishDTO dto);
    Page<GoodsVO> list(Integer current, Integer size);
    Page<GoodsVO> search(GoodsSearchDTO dto);
    Page<GoodsVO> myGoods(Integer current, Integer size);
    void delete(Long id);
    void off(Long id);
    void update(Long id, GoodsUpdateDTO dto);
    GoodsDetailVO detail(Long id);
}
