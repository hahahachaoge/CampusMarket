package com.campus.controller;

import com.campus.common.Result;
import com.campus.dto.LoginDTO;
import com.campus.dto.RegisterDTO;
import com.campus.dto.UpdatePasswordDTO;
import com.campus.dto.UserUpdateDTO;
import com.campus.service.UserService;
import com.campus.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户模块",description = "注册、登录、个人信息、密码修改")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "用户注册", description = "输入用户名、密码、昵称完成账号注册")
    @PostMapping("/register")
    public Result<String> register(
            @RequestBody RegisterDTO dto
    ){
        userService.register(dto);
        return Result.success("注册成功");
    }

    @Operation(summary = "用户登录", description = "用户名密码验证，返回登录token")
    @PostMapping("/login")
    public Result<String> login(
            @RequestBody LoginDTO dto
    ){
        String token = userService.login(dto);
        return Result.success(token);
    }

    @Operation(summary = "获取当前用户信息", description = "根据token获取登录用户的个人资料")
    @GetMapping("/info")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<UserVO> info(
            @RequestHeader("token")
            String token
    ){
        return Result.success(
                userService.info()
        );
    }

    @Operation(summary = "修改个人信息", description = "修改昵称、头像、手机号等个人资料")
    @PutMapping("/update")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<String> update(
            @RequestHeader("token")
            String token,

            @RequestBody
            UserUpdateDTO dto
    ){
        userService.update(dto);
        return Result.success("修改成功");
    }

    @Operation(summary = "修改登录密码", description = "验证旧密码，设置新密码")
    @PutMapping("/password")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<String> updatePassword(
            @RequestHeader("token")
            String token,

            @RequestBody
            UpdatePasswordDTO dto
    ){
        userService.updatePassword(dto);
        return Result.success("密码修改成功");
    }
}
