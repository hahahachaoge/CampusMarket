package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.context.UserContext;
import com.campus.entity.Favorite;
import com.campus.entity.Goods;
import com.campus.mapper.FavoriteMapper;
import com.campus.mapper.GoodsMapper;
import com.campus.service.FavoriteService;
import com.campus.vo.GoodsVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteMapper favoriteMapper;
    private final GoodsMapper goodsMapper;

    @Override
    public void add(Long goodsId){
        Long userId = UserContext.getUserId();
        //判断是否已收藏
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId,userId);
        wrapper.eq(Favorite::getGoodsId,goodsId);
        Favorite favorite = favoriteMapper.selectOne(wrapper);
        if(favorite != null){
            throw new RuntimeException("已经收藏过了");
        }
        Favorite entity = new Favorite();
        entity.setUserId(userId);
        entity.setGoodsId(goodsId);
        favoriteMapper.insert(entity);
    }

    @Override
    public void remove(Long goodsId){
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId,userId);
        wrapper.eq(Favorite::getGoodsId,goodsId);
        favoriteMapper.delete(wrapper);
    }

    @Override
    public Page<GoodsVO> myFavorite(
            Integer current,Integer size
    ){
        Long userId = UserContext.getUserId();
        //收藏分页
        Page<Favorite> page = new Page<>(current,size);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId,userId);
        wrapper.orderByDesc(Favorite::getCreateTime);
        favoriteMapper.selectPage(page,wrapper);
        //没有收藏
        if(page.getRecords().isEmpty()){
            return new Page<>();
        }
        //提取商品ID
        List<Long> goodsIds = page.getRecords().stream().map(Favorite::getGoodsId).toList();
        //查询商品
        List<Goods> goodsList = goodsMapper.selectBatchIds(goodsIds);
        //转VO
        List<GoodsVO> voList = goodsList.stream().map(goods -> {
            GoodsVO vo = new GoodsVO();
            BeanUtils.copyProperties(goods,vo);
            //状态文字
            if(goods.getStatus() == 1){
                vo.setStatusText("在售");
            }
            else if(goods.getStatus() == 2){
                vo.setStatusText("已下架");
            }
            else if(goods.getStatus() == 3){
                vo.setStatusText("已售出");
            }
            return vo;
        }).toList();
        //封装分页结果
        Page<GoodsVO> result = new Page<>();
        result.setCurrent(page.getCurrent());
        result.setPages(page.getPages());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setRecords(voList);
        return result;
    }
}
