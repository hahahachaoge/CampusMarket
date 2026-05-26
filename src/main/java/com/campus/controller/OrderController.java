package com.campus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.Result;
import com.campus.service.OrderService;
import com.campus.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单模块", description = "创建订单、支付、取消、我的订单")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
        private final OrderService orderService;

        @Operation(summary = "创建订单", description = "购买商品，生成待支付订单")
        @PostMapping("/create/{goodsId}")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<String> create(
                        @PathVariable Long goodsId,
                        @RequestHeader("token") String token) {
                orderService.create(goodsId);
                return Result.success("下单成功");
        }

        @Operation(summary = "查询我买到的订单", description = "分页获取买家视角的订单列表")
        @GetMapping("/my/buyer")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<Page<OrderVO>> myBuyerOrders(
                        @RequestHeader("token") String token,

                        @RequestParam(defaultValue = "1") Integer current,

                        @RequestParam(defaultValue = "10") Integer size) {
                return Result.success(orderService.myBuyerOrders(current, size));
        }

        @Operation(summary = "查询我卖出的订单", description = "分页获取卖家视角的订单列表")
        @GetMapping("/my/seller")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<Page<OrderVO>> mySellerOrders(
                        @RequestHeader("token") String token,

                        @RequestParam(defaultValue = "1") Integer current,

                        @RequestParam(defaultValue = "10") Integer size) {
                return Result.success(orderService.mysellerOrders(current, size));
        }

        @Operation(summary = "订单支付", description = "将待支付订单修改为已支付状态")
        @PutMapping("/pay/{orderId}")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<String> pay(
                        @PathVariable Long orderId,
                        @RequestHeader("token") String token) {
                orderService.pay(orderId);
                return Result.success("支付成功");
        }

        @Operation(summary = "完成订单", description = "确认收货，订单标记为已完成")
        @PutMapping("/finish/{orderId}")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<String> finish(
                        @PathVariable Long orderId,
                        @RequestHeader("token") String token) {
                orderService.finish(orderId);
                return Result.success("订单已完成");
        }

        @Operation(summary = "取消订单", description = "取消未支付的订单，商品恢复在售")
        @PutMapping("/cancel/{orderId}")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<String> cancel(
                        @PathVariable Long orderId,
                        @RequestHeader("token") String token) {
                orderService.cancel(orderId);
                return Result.success("订单已取消");
        }
}
