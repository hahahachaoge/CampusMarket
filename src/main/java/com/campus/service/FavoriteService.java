package com.campus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.vo.GoodsVO;

public interface FavoriteService {
    //收藏
    void add(Long goodsId);
    //取消收藏
    void remove(Long goodsId);
    Page<GoodsVO> myFavorite(Integer current, Integer size);
}

