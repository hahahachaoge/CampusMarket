package com.campus.config;

import com.campus.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(
            InterceptorRegistry registry
    ){
        registry.addInterceptor(new JwtInterceptor())
            //拦截所有请求
            .addPathPatterns("/**").excludePathPatterns(
                  "/user/login",
                  "/user/register",
                  "/goods/list",
                  "/goods/detail/**",
                  "/upload",
                  "/upload/**",
                  "/doc.html",
                  "/webjars/**",
                  "/v3/api-docs/**",
                  "/goods/search",
                  "/swagger-resources/**",
                  "/swagger-ui/**",
                  "/favicon.ico",
                  "/test"
                );
    }
}
