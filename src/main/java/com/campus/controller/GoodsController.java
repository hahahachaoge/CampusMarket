package com.campus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.Result;
import com.campus.dto.GoodsPublishDTO;
import com.campus.dto.GoodsSearchDTO;
import com.campus.dto.GoodsUpdateDTO;
import com.campus.service.GoodsService;
import com.campus.vo.GoodsDetailVO;
import com.campus.vo.GoodsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品模块", description = "发布、列表、详情、搜索、我的发布")
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;
    @Operation(summary = "发布商品", description = "登录后发布二手商品，填写标题、价格、描述等")
    @PostMapping("/publish")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<String> publish(
            @RequestBody GoodsPublishDTO dto
    ){
        goodsService.publish(dto);
        return Result.success("发布成功");
    }

    @Operation(summary = "分页查询商品列表", description = "支持分页，默认查询所有在售商品")
    @GetMapping("/list")
    public Result<Page<GoodsVO>> list(
            @RequestParam(defaultValue = "1")
            Integer current,

            @RequestParam(defaultValue = "10")
            Integer size
    ){
        return Result.success(
            goodsService.list(current,size)
        );
    }

    @Operation(summary = "条件搜索商品", description = "支持关键词、分类、价格区间筛选")
    @GetMapping("/search")
    public Result<Page<GoodsVO>> search(
            @ParameterObject GoodsSearchDTO dto
    ){
        return Result.success(
                goodsService.search(dto)
        );
    }

    @Operation(summary = "查询我的商品发布", description = "查看自己发布的所有商品")
    @GetMapping("/my")
    public Result<Page<GoodsVO>> myGoods(
            @RequestHeader("token")
            String token,
            @RequestParam(defaultValue = "1")
            Integer current,

            @RequestParam(defaultValue = "10")
            Integer size
    ){
        return Result.success(goodsService.myGoods(current,size));
    }

    @Operation(summary = "删除商品", description = "只能删除自己发布的商品")
    @DeleteMapping("/{id}")
    public Result<String> delete(
            @PathVariable Long id,
            @RequestHeader("token")
            String token
    ){
        goodsService.delete(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "下架商品", description = "将在售商品修改为下架状态")
    @PutMapping("/off/{id}")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<String> off(
            @PathVariable Long id,
            @RequestHeader("token")
            String token
    ){
        goodsService.off(id);
        return Result.success("下架成功");
    }

    @Operation(summary = "修改商品信息", description = "修改已发布商品的标题、价格、描述等信息")
    @PutMapping("/update/{id}")
    @Parameter(name = "token",description = "用户token",required = true,in = ParameterIn.HEADER)
    public Result<String> update(
            @PathVariable Long id,
            @RequestHeader("token")
            String token,
            @RequestBody
            GoodsUpdateDTO dto
    ){
        goodsService.update(id,dto);
        return Result.success("修改成功");
    }

    @Operation(summary = "查询商品详情", description = "根据商品ID查看详情，浏览量自动+1")
    @GetMapping("/detail/{id}")
    public Result<GoodsDetailVO> detail(@PathVariable Long id){
        return Result.success(goodsService.detail(id));
    }
}
