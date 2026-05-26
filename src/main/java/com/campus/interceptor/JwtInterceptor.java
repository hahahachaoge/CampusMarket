package com.campus.interceptor;

import com.campus.context.UserContext;
import com.campus.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ){

        // 获取token
        String token = request.getHeader("token");

        if(token == null || token.isEmpty()){
            throw new RuntimeException("请先登录");
        }

        //解析token
        Long userId = JwtUtils.parseToken(token);

        // 存入上下文
        UserContext.setUserId(userId);

        return true;
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