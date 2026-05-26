package com.campus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.Result;
import com.campus.service.FavoriteService;
import com.campus.vo.GoodsVO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

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

    @DeleteMapping("/{goodsId}")
    public Result<String> remove(
            @PathVariable Long goodsId
    ){
        favoriteService.remove(goodsId);
        return Result.success("取消收藏成功");
    }

    //我的收藏
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
