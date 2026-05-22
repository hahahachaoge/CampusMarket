package com.campus.controller;

import com.campus.common.Result;
import com.campus.dto.LoginDTO;
import com.campus.dto.RegisterDTO;
import com.campus.dto.UpdatePasswordDTO;
import com.campus.dto.UserUpdateDTO;
import com.campus.service.UserService;
import com.campus.vo.UserVO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Result<String> register(
            @RequestBody RegisterDTO dto
    ){
        userService.register(dto);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<String> login(
            @RequestBody LoginDTO dto
    ){
        String token = userService.login(dto);
        return Result.success(token);
    }

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
