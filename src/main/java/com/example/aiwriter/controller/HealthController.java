package com.example.aiwriter.controller;

import com.example.aiwriter.config.AiProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "健康检查", description = "服务状态和配置信息")
public class HealthController {

    private final AiProperties properties;

    public HealthController(AiProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查服务是否正常运行")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "AI Writing Assistant");
        response.put("provider", properties.getType().toUpperCase());
        response.put("apiKeyConfigured", isApiKeyConfigured());
        response.put("baseUrl", getCurrentBaseUrl());
        response.put("model", getCurrentModel());
        return ResponseEntity.ok(response);
    }

    private boolean isApiKeyConfigured() {
        String apiKey;
        if ("deepseek".equals(properties.getType())) {
            apiKey = properties.getDeepseek().getApiKey();
        } else {
            apiKey = properties.getOpenai().getApiKey();
        }
        return apiKey != null && !apiKey.isEmpty() && !apiKey.equals("your-api-key-here");
    }

    private String getCurrentBaseUrl() {
        if ("deepseek".equals(properties.getType())) {
            return properties.getDeepseek().getBaseUrl();
        } else {
            return properties.getOpenai().getBaseUrl();
        }
    }

    private String getCurrentModel() {
        if ("deepseek".equals(properties.getType())) {
            return properties.getDeepseek().getModel();
        } else {
            return properties.getOpenai().getModel();
        }
    }
}
