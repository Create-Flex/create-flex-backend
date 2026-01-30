package com.mcn.in4.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";

        // SecurityRequirement 설정 (인증이 필요한 API 표시)
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);

        // SecurityScheme 설정 (JWT Bearer 방식)
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .info(apiInfo()) // API 정보 설정
                .addSecurityItem(securityRequirement) // 인증 요구사항 추가
                .components(components); // 보안 스키마가 담긴 컴포넌트 추가
    }

    private Info apiInfo() {
        return new Info()
                .title("IN4 API 명세서")
                .description("직원 관리 시스템(IN4) API 테스트를 위한 문서입니다.")
                .version("1.0.0");
    }
}