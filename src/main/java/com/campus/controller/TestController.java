package com.campus.controller;

import com.campus.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public Result<String> test(){
        return Result.success("项目启动成功");
    }
}
