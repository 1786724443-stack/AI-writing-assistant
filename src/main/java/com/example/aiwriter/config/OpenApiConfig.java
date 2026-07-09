package com.example.aiwriter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI 写作助手 API")
                        .version("1.0.0")
                        .description("基于大模型 API 的写作助手后端服务，提供文章生成、润色、扩写、摘要、翻译等能力。")
                        .contact(new Contact()
                                .name("AI 写作助手")
                                .email("support@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }

    @Bean
    public GroupedOpenApi writingApi() {
        return GroupedOpenApi.builder()
                .group("写作助手")
                .pathsToMatch("/api/**")
                .build();
    }
}
