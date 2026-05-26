package com.campus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.Result;
import com.campus.service.FavoriteService;
import com.campus.vo.GoodsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@Tag(name = "收藏模块", description = "收藏商品、取消收藏、我的收藏")
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Operation(summary = "收藏商品", description = "添加商品到我的收藏列表")
    @PostMapping("/{goodsId}")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<String> add(
        @PathVariable Long goodsId
    ){
        favoriteService.add(goodsId);
        return Result.success("收藏成功");
    }

    @Operation(summary = "取消收藏商品", description = "将商品从我的收藏中移除")
    @DeleteMapping("/{goodsId}")
    public Result<String> remove(
            @PathVariable Long goodsId
    ){
        favoriteService.remove(goodsId);
        return Result.success("取消收藏成功");
    }

    //我的收藏
    @Operation(summary = "查询我的收藏", description = "分页获取自己收藏的所有商品")
    @GetMapping("/my")
    public Result<Page<GoodsVO>> my(
            @RequestParam(defaultValue = "1")
            Integer current,

            @RequestParam(defaultValue = "10")
            Integer size
    ){
        return Result.success(favoriteService.myFavorite(current, size));
    }
}
