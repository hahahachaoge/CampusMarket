package com.campus.config;

import com.campus.interceptor.JwtInterceptor;
import com.campus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserService userService;
    @Override
    public void addInterceptors(
            InterceptorRegistry registry
    ){
        registry.addInterceptor(new JwtInterceptor(userService))
            //拦截所有请求
            .addPathPatterns("/**")
            //无需登录放行接口
            .excludePathPatterns(
                    "/user/login",
                    "/user/register",
                    "/goods/list",
                    "/goods/detail/**",
                    "/goods/search",
                    "/upload",
                    "/upload/**",
                    "/test",
                    // 接口文档放行
                    "/doc.html",
                    "/webjars/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui/**",
                    "/favicon.ico"
                );
    }
}
