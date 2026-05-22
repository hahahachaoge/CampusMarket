package com.campus.interceptor;

import com.campus.common.Result;
import com.campus.context.UserContext;
import com.campus.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    )throws Exception{
        //获取请求头token
        String token = request.getHeader("token");
        //token为空
        if(token == null || token.isEmpty()){
            response.setContentType("application/json;charset=UTF-8");
            Result<String> result = Result.error("请先登录");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(result)
            );
            return false;
        }

        try{
            //解析token
            Long userId = JwtUtils.parseToken(token);
            //存入ThreadLocal
            UserContext.setUserId(userId);
            return true;
        }
        catch(Exception e){
            response.setContentType("application/json;charset=UTF-8");
            Result<String> result = Result.error("token无效");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(result)
            );
            return false;
        }
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ){
        //防止内存泄露
        UserContext.clear();
    }
}
