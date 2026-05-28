package com.campus.controller;

import com.campus.common.Result;
import com.campus.context.UserContext;
import com.campus.entity.User;
import com.campus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员用户管理",description = "设置用户管理员权限")
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @Operation(summary = "设置用户为管理员")
    @PutMapping("/set-admin")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<?> setAdmin(
            @RequestParam String username,
            @RequestHeader("token") String token
    ){
        User user = userService.getByUsername(username);
        if(user == null){
            return Result.error("用户不存在");
        }
        user.setRole(2);
        userService.updateById(user);
        return Result.success("已成功将用户["+username+"]设置为管理员");
    }

    @Operation(summary = "取消用户管理员权限")
    @PutMapping("/unset-admin")
    @Parameter(
            name = "token",
            description = "用户token",
            required = true,
            in = ParameterIn.HEADER
    )
    public Result<?> unsetAdmin(
            @RequestParam String username,
            @RequestHeader("token") String token
    ){
        User targetUser = userService.getByUsername(username);
        if(targetUser == null){
            return Result.error("用户不存在");
        }
        //管理员不能取消自己的管理员身份
        Long currentAdminId = UserContext.getUserId();
        if(targetUser.getId().equals(currentAdminId)){
            return Result.error("禁止操作：你不能取消自己的管理员权限");
        }
        targetUser.setRole(1);
        userService.updateById(targetUser);
        return Result.success("已成功取消用户[" + username + "]的管理员权限");
    }
}
