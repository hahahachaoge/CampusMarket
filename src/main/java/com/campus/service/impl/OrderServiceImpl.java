package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.context.UserContext;
import com.campus.entity.Goods;
import com.campus.entity.Order;
import com.campus.entity.User;
import com.campus.mapper.GoodsMapper;
import com.campus.mapper.OrderMapper;
import com.campus.mapper.UserMapper;
import com.campus.service.OrderService;
import com.campus.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final GoodsMapper goodsMapper;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void create(Long goodsId){
        //当前用户
        Long buyerId = UserContext.getUserId();
        //查询商品
        Goods goods = goodsMapper.selectById(goodsId);
        //商品不存在
        if(goods == null){
            throw new RuntimeException("商品不存在");
        }
        //商品已下架
        if(goods.getUserId().equals(buyerId)){
            throw new RuntimeException("不能购买自己的商品");
        }
        //创建订单
        Order order = new Order();
        //订单号
        order.setOrderNo(UUID.randomUUID().toString().replace("-",""));
        //买家
        order.setBuyerId(buyerId);
        //卖家
        order.setSellerId(goods.getUserId());
        //商品
        order.setGoodsId(goodsId);
        //价格
        order.setPrice(goods.getPrice());
        //待支付
        order.setStatus(0);
        orderMapper.insert(order);
        //商品改为已售出
        goods.setStatus(3);
        goodsMapper.updateById(goods);
    }

    @Override
    public Page<OrderVO> myBuyerOrders(Integer current, Integer size){
        Long buyerId = UserContext.getUserId();
        //分页
        Page<Order> page = new Page<>(current,size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getBuyerId,buyerId);
        wrapper.orderByDesc(Order::getCreateTime);
        orderMapper.selectPage(page,wrapper);
        //转VO
        List<OrderVO> voList = page.getRecords().stream().map(order -> {
            OrderVO vo = new OrderVO();
            //订单信息
            vo.setId(order.getId());
            vo.setOrderNo(order.getOrderNo());
            vo.setPrice(order.getPrice());
            vo.setStatus(order.getStatus());
            vo.setCreateTime(order.getCreateTime());
            //状态文字
            if(order.getStatus() == 0){
                vo.setStatusText("待支付");
            }
            else if(order.getStatus() == 1){
                vo.setStatusText("已支付");
            }
            else if(order.getStatus() == 2){
                vo.setStatusText("已完成");
            }
            else if(order.getStatus() == 3){
                vo.setStatusText("已取消");
            }
            //查询商品
            Goods goods = goodsMapper.selectById(order.getGoodsId());
            if(goods != null){
                vo.setGoodsId(goods.getId());
                vo.setGoodsTitle(goods.getTitle());
                vo.setGoodsCover(goods.getCover());
            }
            //查询卖家
            User seller = userMapper.selectById(order.getSellerId());
            if(seller != null){
                vo.setSellerId(seller.getId());
                vo.setSellerNickname(seller.getNickname());
            }
            return vo;
        }).toList();
        //封装分页
        Page<OrderVO> result = new Page<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(voList);
        return result;
    }

    @Override
    public Page<OrderVO> mysellerOrders(Integer current,Integer size){
        Long sellerId = UserContext.getUserId();
        //分页
        Page<Order> page = new Page<>(current,size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getSellerId,sellerId);
        wrapper.orderByDesc(Order::getCreateTime);
        orderMapper.selectPage(page,wrapper);
        //转VO
        List<OrderVO> voList = page.getRecords().stream().map(order ->{
            OrderVO vo = new OrderVO();
            //订单信息
            vo.setId(order.getId());
            vo.setOrderNo(order.getOrderNo());
            vo.setPrice(order.getPrice());
            vo.setStatus(order.getStatus());
            vo.setCreateTime(order.getCreateTime());
            //状态文字
            if(order.getStatus() == 0){
                vo.setStatusText("待支付");
            }
            else if(order.getStatus() == 1){
                vo.setStatusText("已支付");
            }
            else if(order.getStatus() == 2){
                vo.setStatusText("已完成");
            }
            else if(order.getStatus() == 3){
                vo.setStatusText("已取消");
            }
            //商品信息
            Goods goods = goodsMapper.selectById(order.getGoodsId());
            if(goods != null){
                vo.setGoodsId(goods.getId());
                vo.setGoodsTitle(goods.getTitle());
                vo.setGoodsCover(goods.getCover());
            }
            //买家信息
            User buyer = userMapper.selectById(order.getBuyerId());
            if(buyer != null){
                vo.setBuyerId(buyer.getId());
                vo.setSellerNickname(buyer.getNickname());
            }
            return vo;
        }).toList();
        //封装分页
        Page<OrderVO> result = new Page<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(voList);
        return result;
    }

    @Override
    @Transactional
    public void pay(Long orderId){
        Long userId = UserContext.getUserId();
        //查询订单
        Order order = orderMapper.selectById(orderId);
        //订单不存在
        if(order == null){
            throw new RuntimeException("订单不存在");
        }
        //只能支付自己的订单
        if(!order.getBuyerId().equals(userId)){
            throw new RuntimeException("无权限操作");
        }
        //必须是待支付
        if(order.getStatus() != 0){
            throw new RuntimeException("订单状态错误");
        }
        //修改状态
        order.setStatus(1);
        orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void finish(Long orderId){
        Long userId = UserContext.getUserId();
        Order order = orderMapper.selectById(orderId);
        if(order == null){
            throw new RuntimeException("订单不存在");
        }
        //只能买家完成订单
        if(!order.getBuyerId().equals(userId)){
            throw new RuntimeException("无权限操作");
        }
        //必须已支付
        if(order.getStatus() != 1){
            throw new RuntimeException("订单状态错误");
        }
        //已完成
        order.setStatus(2);
        orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void cancel(Long orderId){
        Long userId = UserContext.getUserId();
        Order order = orderMapper.selectById(orderId);
        if(order == null){
            throw new RuntimeException("订单不存在");
        }
        //必须本人
        if(!order.getBuyerId().equals(userId)){
            throw new RuntimeException("无权限操作");
        }
        //只能取消待支付
        if(order.getStatus() != 0){
            throw new RuntimeException("当前订单无法取消");
        }
        //修改订单状态
        order.setStatus(3);
        orderMapper.updateById(order);
        //商品恢复在售
        Goods goods = goodsMapper.selectById(order.getGoodsId());
        if(goods != null){
            goods.setStatus(1);
            goodsMapper.updateById(goods);
        }
    }
}
