package com.dean.baby.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
                        .description("API documentation for Clockin application"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (operation, handlerMethod) -> {
            Parameter acceptLanguageParam = new Parameter()
                    .in("header")
                    .name("Accept-Language")
                    .description("語言設定 - 支援的語言代碼: tw(繁體中文), cn(簡體中文), en(英文), ja(日文), ko(韓文), vi(越南文)")
                    .required(false)
                    .schema(new StringSchema()
                            .example("tw")
                            ._default("tw")
                            ._enum(java.util.Arrays.asList("tw", "cn", "en", "ja", "ko", "vi")));

            operation.addParametersItem(acceptLanguageParam);
            return operation;
        };
    }
}
