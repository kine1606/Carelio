package com.Carelio.user_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceOpenAPI() {
        final String securitySchemeName = "Keycloak_JWT_Auth";

        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .version("1.0.0")
                        .description("API documentation for Carelio User Service with Keycloak Integration"))
                // 1. Thêm yêu cầu bảo mật mặc định cho tất cả API trên Swagger
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 2. Định nghĩa cấu hình nút ổ khóa nhập Token
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Dán chuỗi access_token lấy từ Keycloak vào đây để test API.")));
    }
}