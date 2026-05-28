package com.campus.interceptor;

import com.campus.common.Result;
import com.campus.context.UserContext;
import com.campus.entity.User;
import com.campus.enums.UserRoleEnum;
import com.campus.service.UserService;
import com.campus.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

public class JwtInterceptor implements HandlerInterceptor {
    private final UserService userService;
    public JwtInterceptor(UserService userService){
        this.userService = userService;
    }
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ){
        // 设置响应编码
        response.setContentType("application/json;charset=UTF-8");

        // 获取token
        String token = request.getHeader("token");

        if(token == null || token.isEmpty()){
            writeResult(response, Result.unauthorized("请先登录"));
            return false;
        }

        try {
            //解析token
            Long userId = JwtUtils.parseToken(token);
            // 存入上下文
            UserContext.setUserId(userId);
            // 管理员权限校验
            String requestUri = request.getRequestURI();
            if(requestUri.startsWith("/admin")){
                User user = userService.getById(userId);
                if(!UserRoleEnum.ADMIN.getCode().equals(user.getRole())){
                    writeResult(response,Result.forbidden("无管理员权限"));
                    return false;
                }
            }
        }catch (Exception e){
            writeResult(response, Result.unauthorized("token无效，请重新登录"));
            return false;
        }
        return true;
    }

    // 输出JSON给前端
    private void writeResult(HttpServletResponse response,Result<?> result){
        try(PrintWriter writer = response.getWriter()){
            writer.print("{\"code\":"+result.getCode()+",\"message\":\""+result.getMessage()+"\",\"data\":null}");
        }
        catch(Exception ignored){
        }
    }

    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            Exception ex
    ){
        // 清理ThreadLocal
        UserContext.clear();
    }
}