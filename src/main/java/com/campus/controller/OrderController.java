package com.campus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.Result;
import com.campus.service.OrderService;
import com.campus.vo.OrderVO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
        private final OrderService orderService;

        @PostMapping("/create/{goodsId}")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<String> create(
                        @PathVariable Long goodsId,
                        @RequestHeader("token") String token) {
                orderService.create(goodsId);
                return Result.success("下单成功");
        }

        @GetMapping("/my/buyer")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<Page<OrderVO>> myBuyerOrders(
                        @RequestHeader("token") String token,

                        @RequestParam(defaultValue = "1") Integer current,

                        @RequestParam(defaultValue = "10") Integer size) {
                return Result.success(orderService.myBuyerOrders(current, size));
        }

        @GetMapping("/my/seller")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<Page<OrderVO>> mySellerOrders(
                        @RequestHeader("token") String token,

                        @RequestParam(defaultValue = "1") Integer current,

                        @RequestParam(defaultValue = "10") Integer size) {
                return Result.success(orderService.mysellerOrders(current, size));
        }

        @PutMapping("/pay/{orderId}")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<String> pay(
                        @PathVariable Long orderId,
                        @RequestHeader("token") String token) {
                orderService.pay(orderId);
                return Result.success("支付成功");
        }

        @PutMapping("/finish/{orderId}")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<String> finish(
                        @PathVariable Long orderId,
                        @RequestHeader("token") String token) {
                orderService.finish(orderId);
                return Result.success("订单已完成");
        }

        @PutMapping("/cancel/{orderId}")
        @Parameter(name = "token", description = "用户token", required = true, in = ParameterIn.HEADER)
        public Result<String> cancel(
                        @PathVariable Long orderId,
                        @RequestHeader("token") String token) {
                orderService.cancel(orderId);
                return Result.success("订单已取消");
        }
}
