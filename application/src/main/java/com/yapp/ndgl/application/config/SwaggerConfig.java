package com.yapp.ndgl.application.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NDGL API")
                        .description("NDGL API 문서")
                        .version("v1.0.0"));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
            .group("api")
            .packagesToScan("com.yapp.ndgl.application")
            .pathsToMatch("/api/**")
            .build();
    }
}
