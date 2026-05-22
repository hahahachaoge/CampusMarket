package com.campus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.Result;
import com.campus.dto.GoodsPublishDTO;
import com.campus.dto.GoodsSearchDTO;
import com.campus.dto.GoodsUpdateDTO;
import com.campus.service.GoodsService;
import com.campus.vo.GoodsDetailVO;
import com.campus.vo.GoodsVO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

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

    @GetMapping("/search")
    public Result<Page<GoodsVO>> search(
            @ParameterObject GoodsSearchDTO dto
    ){
        return Result.success(
                goodsService.search(dto)
        );
    }

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

    @DeleteMapping("/{id}")
    public Result<String> delete(
            @PathVariable Long id,
            @RequestHeader("token")
            String token
    ){
        goodsService.delete(id);
        return Result.success("删除成功");
    }

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

    @GetMapping("/detail/{id}")
    public Result<GoodsDetailVO> detail(@PathVariable Long id){
        return Result.success(goodsService.detail(id));
    }
}
