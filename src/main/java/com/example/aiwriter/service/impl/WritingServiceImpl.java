package com.example.aiwriter.service.impl;

import com.example.aiwriter.client.AiClient;
import com.example.aiwriter.client.AiClientFactory;
import com.example.aiwriter.client.model.AiResponse;
import com.example.aiwriter.dto.request.*;
import com.example.aiwriter.dto.response.WritingResponse;
import com.example.aiwriter.entity.ApiCallLog;
import com.example.aiwriter.entity.User;
import com.example.aiwriter.prompt.PromptTemplate;
import com.example.aiwriter.repository.ApiCallLogRepository;
import com.example.aiwriter.repository.UserRepository;
import com.example.aiwriter.service.WritingService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WritingServiceImpl implements WritingService {

    private static final Logger log = LoggerFactory.getLogger(WritingServiceImpl.class);

    private final AiClientFactory clientFactory;
    private final UserRepository userRepository;
    private final ApiCallLogRepository apiCallLogRepository;

    public WritingServiceImpl(AiClientFactory clientFactory, 
                             UserRepository userRepository,
                             ApiCallLogRepository apiCallLogRepository) {
        this.clientFactory = clientFactory;
        this.userRepository = userRepository;
        this.apiCallLogRepository = apiCallLogRepository;
    }

    @Override
    @Transactional
    public WritingResponse generate(GenerateRequest request) {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        Long userId = getCurrentUserId();
        String endpoint = "/api/generate";

        log.info("Generating article for topic: {}, requestId: {}", request.getTopic(), requestId);

        String prompt = buildPrompt(PromptTemplate.GENERATE, request.getTopic(), 
                request.getStyle() != null ? request.getStyle() : "正式、专业、结构清晰");
        AiClient client = clientFactory.getClient();

        AiResponse aiResponse = client.generate(prompt);
        WritingResponse response = buildResponse(requestId, aiResponse, startTime);

        recordApiCall(userId, endpoint, requestId, response.getContent(), 
                response.getTokenCount(), response.getDuration(), "SUCCESS", null);
        deductQuota(userId);

        return response;
    }

    @Override
    @Transactional
    public WritingResponse polish(PolishRequest request) {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        Long userId = getCurrentUserId();
        String endpoint = "/api/polish";

        log.info("Polishing article, requestId: {}, contentLength: {}", requestId, request.getContent().length());

        String prompt = buildPrompt(PromptTemplate.POLISH, request.getContent(), 
                request.getStyle() != null ? request.getStyle() : "正式、专业");
        AiClient client = clientFactory.getClient();

        AiResponse aiResponse = client.generate(prompt);
        WritingResponse response = buildResponse(requestId, aiResponse, startTime);

        recordApiCall(userId, endpoint, requestId, response.getContent(), 
                response.getTokenCount(), response.getDuration(), "SUCCESS", null);
        deductQuota(userId);

        return response;
    }

    @Override
    @Transactional
    public WritingResponse expand(ExpandRequest request) {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        Long userId = getCurrentUserId();
        String endpoint = "/api/expand";

        log.info("Expanding article, requestId: {}, targetLength: {}", requestId, request.getTargetLength());

        String prompt = buildPrompt(PromptTemplate.EXPAND, 
                request.getTargetLength() != null ? request.getTargetLength() : 1500,
                request.getContent());
        AiClient client = clientFactory.getClient();

        AiResponse aiResponse = client.generate(prompt);
        WritingResponse response = buildResponse(requestId, aiResponse, startTime);

        recordApiCall(userId, endpoint, requestId, response.getContent(), 
                response.getTokenCount(), response.getDuration(), "SUCCESS", null);
        deductQuota(userId);

        return response;
    }

    @Override
    @Transactional
    public WritingResponse summarize(SummarizeRequest request) {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        Long userId = getCurrentUserId();
        String endpoint = "/api/summarize";

        log.info("Summarizing article, requestId: {}, maxLength: {}", requestId, request.getMaxLength());

        String prompt = buildPrompt(PromptTemplate.SUMMARIZE, 
                request.getMaxLength() != null ? request.getMaxLength() : 300,
                request.getContent());
        AiClient client = clientFactory.getClient();

        AiResponse aiResponse = client.generate(prompt);
        WritingResponse response = buildResponse(requestId, aiResponse, startTime);

        recordApiCall(userId, endpoint, requestId, response.getContent(), 
                response.getTokenCount(), response.getDuration(), "SUCCESS", null);
        deductQuota(userId);

        return response;
    }

    @Override
    @Transactional
    public WritingResponse translate(TranslateRequest request) {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        Long userId = getCurrentUserId();
        String endpoint = "/api/translate";

        log.info("Translating article, requestId: {}, targetLanguage: {}", requestId, request.getTargetLanguage());

        String prompt = buildPrompt(PromptTemplate.TRANSLATE, request.getContent(), request.getTargetLanguage());
        AiClient client = clientFactory.getClient();

        AiResponse aiResponse = client.generate(prompt);
        WritingResponse response = buildResponse(requestId, aiResponse, startTime);

        recordApiCall(userId, endpoint, requestId, response.getContent(), 
                response.getTokenCount(), response.getDuration(), "SUCCESS", null);
        deductQuota(userId);

        return response;
    }

    private Long getCurrentUserId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return (Long) request.getAttribute("userId");
            }
        } catch (Exception e) {
            log.warn("Cannot get current user ID: {}", e.getMessage());
        }
        return null;
    }

    private void recordApiCall(Long userId, String endpoint, String requestId, 
                               String content, Integer tokenCount, Long durationMs, 
                               String status, String errorMessage) {
        if (userId != null) {
            ApiCallLog logEntry = new ApiCallLog();
            logEntry.setUserId(userId);
            logEntry.setEndpoint(endpoint);
            logEntry.setRequestId(requestId);
            logEntry.setContent(content != null && content.length() > 500 ? content.substring(0, 500) + "..." : content);
            logEntry.setTokenCount(tokenCount);
            logEntry.setDurationMs(durationMs);
            logEntry.setStatus(status);
            logEntry.setErrorMessage(errorMessage);
            apiCallLogRepository.save(logEntry);
        }
    }

    private void deductQuota(Long userId) {
        if (userId != null) {
            userRepository.incrementUsedQuota(userId);
        }
    }

    private String buildPrompt(PromptTemplate template, Object... args) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(template.getSystemPrompt()).append("\n\n");
        prompt.append(template.getUserPrompt(args));
        return prompt.toString();
    }

    private WritingResponse buildResponse(String requestId, AiResponse aiResponse, long startTime) {
        long duration = System.currentTimeMillis() - startTime;

        if (aiResponse.getSuccess()) {
            log.info("Operation successful, requestId: {}, duration: {}ms, tokens: {}",
                    requestId, duration, aiResponse.getTokenUsage());
            return WritingResponse.builder()
                    .id(requestId)
                    .content(aiResponse.getContent())
                    .tokenCount(aiResponse.getTokenUsage())
                    .duration(duration)
                    .createdAt(LocalDateTime.now())
                    .build();
        } else {
            log.error("Operation failed, requestId: {}, error: {}",
                    requestId, aiResponse.getErrorMessage());
            throw new RuntimeException("Failed: " + aiResponse.getErrorMessage());
        }
    }
}
