package com.example.aiwriter.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AiApiKeyValidator {

    private static final Logger log = LoggerFactory.getLogger(AiApiKeyValidator.class);

    private final AiProperties properties;

    public AiApiKeyValidator(AiProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void validateApiKey() {
        String provider = properties.getType();
        String apiKey;

        if ("openai".equalsIgnoreCase(provider)) {
            apiKey = properties.getOpenai().getApiKey();
        } else {
            apiKey = properties.getDeepseek().getApiKey();
        }

        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here")) {
            log.warn("=========================================================");
            log.warn("警告: AI API Key 未配置!");
            log.warn("当前提供商: {}", provider.toUpperCase());
            log.warn("请设置环境变量: {}", provider.equals("openai") ? "OPENAI_API_KEY" : "DEEPSEEK_API_KEY");
            log.warn("或在 application.yml 中配置 ai.provider.{}", provider);
            log.warn("示例:");
            log.warn("  DEEPSEEK_API_KEY=sk-xxx");
            log.warn("服务仍将启动,但调用 AI 接口时会失败!");
            log.warn("=========================================================");
        } else {
            log.info("AI API Key 已配置 ({}), 提供商: {}", 
                    maskApiKey(apiKey), provider.toUpperCase());
        }
    }

    private String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() <= 5) {
            return "****";
        }
        return apiKey.substring(0, 5) + "****";
    }
}
