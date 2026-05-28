package com.campus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.Result;
import com.campus.entity.Goods;
import com.campus.entity.Order;
import com.campus.entity.User;
import com.campus.service.GoodsService;
import com.campus.service.OrderService;
import com.campus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员后台",description = "用户/商品/订单管理")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final GoodsService goodsService;
    private final OrderService orderService;

    @Operation(summary = "查询所有用户列表")
    @GetMapping("/user/list/{current}/{size}")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<Page<User>> userList(@PathVariable Integer current, @PathVariable Integer size, @RequestHeader("token") String token){
        return Result.success(userService.page(new Page<>(current,size)));
    }

    @Operation(summary = "查询所有商品列表")
    @GetMapping("/goods/list/{current}/{size}")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<Page<Goods>> goodsList(@PathVariable Integer current, @PathVariable Integer size, @RequestHeader("token") String token) {
        return Result.success(goodsService.page(new Page<>(current, size)));
    }

    @Operation(summary = "查询所有订单列表")
    @GetMapping("/order/list/{current}/{size}")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<Page<Order>> orderList(@PathVariable Integer current, @PathVariable Integer size, @RequestHeader("token") String token) {
        return Result.success(orderService.page(new Page<>(current, size)));
    }
}
