package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.context.UserContext;
import com.campus.dto.GoodsPublishDTO;
import com.campus.dto.GoodsSearchDTO;
import com.campus.dto.GoodsUpdateDTO;
import com.campus.entity.Goods;
import com.campus.entity.User;
import com.campus.enums.GoodsStatusEnum;
import com.campus.mapper.UserMapper;
import com.campus.mapper.GoodsMapper;
import com.campus.service.GoodsService;
import com.campus.vo.GoodsDetailVO;
import com.campus.vo.GoodsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {
    private final GoodsMapper goodsMapper;
    private final UserMapper userMapper;

    @Override
    public void publish(GoodsPublishDTO dto){
        Goods goods = new Goods();
        goods.setUserId(
                UserContext.getUserId()
        );
        goods.setTitle(dto.getTitle());
        goods.setDescription(dto.getDescription());
        goods.setPrice(dto.getPrice());
        goods.setOriginalPrice(dto.getOriginalPrice());
        goods.setCategory(dto.getCategory());
        goods.setCover(dto.getCover());
        goods.setStatus(GoodsStatusEnum.ON_SALE.getCode());
        goods.setViewCount(0);
        goodsMapper.insert(goods);
    }

    @Override
    public Page<GoodsVO> list(
            Integer current,
            Integer size
    ){
        Page<Goods> page = new Page<>(current,size);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getStatus,GoodsStatusEnum.ON_SALE.getCode());
        //最新发布
        wrapper.orderByDesc(Goods::getCreateTime);
        goodsMapper.selectPage(page,wrapper);
        Page<GoodsVO> result = new Page<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(
                page.getRecords().stream().map(goods -> {
                    GoodsVO vo = new GoodsVO();
                    vo.setId(goods.getId());
                    vo.setTitle(goods.getTitle());
                    vo.setDescription(goods.getDescription());
                    vo.setPrice(goods.getPrice());
                    vo.setOriginalPrice(goods.getOriginalPrice());
                    vo.setCategory(goods.getCategory());
                    vo.setCover(goods.getCover());
                    vo.setViewCount(goods.getViewCount());
                    vo.setCreateTime(goods.getCreateTime());
                    return vo;
                }).toList()
        );
        return result;
    }

    @Override
    public GoodsDetailVO detail(Long id){
        //查询商品
        Goods goods = goodsMapper.selectById(id);
        if(goods == null){
            throw new RuntimeException("商品不存在");
        }
        //浏览量+1
        goods.setViewCount(goods.getViewCount()+1);
        goodsMapper.updateById(goods);
        //查询用户
        User user = userMapper.selectById(goods.getUserId());
        GoodsDetailVO vo = new GoodsDetailVO();
        BeanUtils.copyProperties(goods,vo);
        //状态文字
        vo.setStatusText(GoodsStatusEnum.getText(goods.getStatus()));
        //发布人信息
        if(user != null){
            vo.setNickname(user.getNickname());
            vo.setAvatar(user.getAvatar());
        }
        return vo;
    }

    @Override
    public Page<GoodsVO> search(
            GoodsSearchDTO dto
    ){
        Page<Goods> page = new Page<>(
                dto.getCurrent(),
                dto.getSize()
        );
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        //只查询在售商品
        wrapper.eq(Goods::getStatus,GoodsStatusEnum.ON_SALE.getCode());
        //关键字搜索
        if(StringUtils.hasText(dto.getKeyword())){
            wrapper.like(
                    Goods::getTitle,
                    dto.getKeyword()
            );
        }
        //分类筛选
        if(StringUtils.hasText(dto.getCategory())){
            wrapper.eq(
                    Goods::getCategory,
                    dto.getCategory()
            );
        }
        //最低价格
        if(dto.getMinPrice()!=null){
            wrapper.ge(
                    Goods::getPrice,
                    dto.getMinPrice()
            );
        }
        //最高价格
        if(dto.getMaxPrice()!=null){
            wrapper.le(
                    Goods::getPrice,
                    dto.getMaxPrice()
            );
        }
        //排序
        if(StringUtils.hasText(dto.getSortBy())){
            boolean asc = "asc".equalsIgnoreCase(dto.getOrder());
            switch(dto.getSortBy()){
                case "price" -> {
                    wrapper.orderBy(
                            true,
                            asc,
                            Goods::getPrice
                    );
                }
                case "viewCount" -> {
                    wrapper.orderBy(
                            true,
                            asc,
                            Goods::getViewCount
                    );
                }
                case "createTime" -> {
                    wrapper.orderBy(
                            true,
                            asc,
                            Goods::getCreateTime
                    );
                }
            }
        }
        else{
            //默认最新发布
            wrapper.orderByDesc(Goods::getCreateTime);
        }
        goodsMapper.selectPage(page,wrapper);
        Page<GoodsVO> result = new Page<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(
            page.getRecords().stream().map(goods -> {
                    GoodsVO vo = new GoodsVO();
                    BeanUtils.copyProperties(goods,vo);
                    //状态
                    vo.setStatus(goods.getStatus());
                    //状态文字
                    vo.setStatusText(GoodsStatusEnum.getText(goods.getStatus()));
                    return vo;
            }).toList()
        );
        return result;
    }

    @Override
    public Page<GoodsVO> myGoods(
            Integer current,
            Integer size
    ){
        //当前登录用户
        Long userId = UserContext.getUserId();
        Page<Goods> page = new Page<>(current,size);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        //只查自己的商品
        wrapper.eq(Goods::getUserId,userId);
        //最新发布
        wrapper.orderByDesc(Goods::getCreateTime);
        goodsMapper.selectPage(page,wrapper);
        Page<GoodsVO> result = new Page<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(
            page.getRecords().stream().map(goods ->{
                GoodsVO vo = new GoodsVO();
                BeanUtils.copyProperties(goods,vo);
                // 商品状态
                vo.setStatus(goods.getStatus());
                // 状态文字
                vo.setStatusText(GoodsStatusEnum.getText(goods.getStatus()));
                return vo;
            }).toList()
        );
        return result;
    }

    @Override
    public void delete(Long id){
        //查询商品
        Goods goods = goodsMapper.selectById(id);
        //商品不存在
        if(goods == null){
            throw new RuntimeException("商品不存在");
        }
        //当前登录用户
        Long currentUserId = UserContext.getUserId();
        //权限校验
        if(!goods.getUserId().equals(currentUserId)){
            throw new RuntimeException("无权限删除该商品");
        }
        //删除商品
        goodsMapper.deleteById(id);
    }

    @Override
    public void off(Long id){
        //查询商品
        Goods goods = goodsMapper.selectById(id);
        if(goods == null){
            throw new RuntimeException("商品不存在");
        }
        //当前用户
        Long currentUserId = UserContext.getUserId();
        //权限校验
        if(!goods.getUserId().equals(currentUserId)){
            throw new RuntimeException("无权限操作");
        }
        //下架
        goods.setStatus(GoodsStatusEnum.OFF_SALE.getCode());
        goodsMapper.updateById(goods);
    }

    @Override
    public void update(Long id, GoodsUpdateDTO dto){
        //查询商品
        Goods goods = goodsMapper.selectById(id);
        //商品不存在
        if(goods == null){
            throw new RuntimeException("商品不存在");
        }
        //当前登录用户
        Long currentUserId = UserContext.getUserId();
        //权限校验
        if(!goods.getUserId().equals(currentUserId)){
            throw new RuntimeException("无权限修改该商品");
        }
        //更新字段
        goods.setTitle(dto.getTitle());
        goods.setDescription(dto.getDescription());
        goods.setPrice(dto.getPrice());
        goods.setOriginalPrice(dto.getOriginalPrice());
        goods.setCategory(dto.getCategory());
        goods.setCover(dto.getCover());
        goodsMapper.updateById(goods);
    }
}
