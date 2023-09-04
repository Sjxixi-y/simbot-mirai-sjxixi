package com.sjxixi.simbotmiraisjxixi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// 激活swagger配置
public class SwaggerConfig {
    // 创建RestApi文档生成
    // @Bean
    // public GroupedOpenApi createRestApi() {
    //     return GroupedOpenApi.builder()
    //             .group("http")
    //             .pathsToMatch("/")
    //             .build();
    // }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Spring Boot 集成 Swagger 构建 RESTFul API")
                .description("QQ 群管理系统")
                .version("1.0"));
    }
}
