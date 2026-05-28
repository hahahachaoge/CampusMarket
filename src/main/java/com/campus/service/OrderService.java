package com.campus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.entity.Order;
import com.campus.vo.OrderVO;

public interface OrderService extends IService<Order> {
    //创建订单
    void create(Long goodsId);
    Page<OrderVO> myBuyerOrders(Integer current, Integer size);
    Page<OrderVO> mysellerOrders(Integer current,Integer size);
    void pay(Long orderId);
    //完成订单
    void finish(Long orderId);
    //取消订单
    void cancel(Long orderId);
}
