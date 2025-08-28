package com.dean.baby.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Baby Development API")
                        .version("1.0")
                        .description("API documentation for Baby Development application"))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth")
                        .addList("Accept-Language"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        .addSecuritySchemes("Accept-Language", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Accept-Language")
                                .description("語言設定 - 支援: tw(繁體中文), cn(簡體中文), en(英文), ja(日文), ko(韓文), vi(越南文)")));
    }

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (operation, handlerMethod) -> {
            // 移除個別的Accept-Language參數，因為現在由Auth統一管理
            return operation;
        };
    }
}
