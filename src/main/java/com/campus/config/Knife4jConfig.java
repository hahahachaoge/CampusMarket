package com.campus.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI(){

        // token请求头
        SecurityScheme securityScheme =
                new SecurityScheme()

                        .type(SecurityScheme.Type.APIKEY)

                        .in(SecurityScheme.In.HEADER)

                        .name("token");

        return new OpenAPI()

                .info(
                        new Info()
                                .title("Campus Market API")
                                .version("1.0")
                                .description("校园二手交易平台接口文档")
                )

                // 全局认证
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("token")
                )

                .schemaRequirement(
                        "token",
                        securityScheme
                );
    }
}