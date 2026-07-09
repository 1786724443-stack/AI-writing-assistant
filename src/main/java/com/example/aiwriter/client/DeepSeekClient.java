package com.example.aiwriter.client;

import com.example.aiwriter.client.model.AiResponse;
import com.example.aiwriter.client.model.OpenAiRequest;
import com.example.aiwriter.client.model.OpenAiResponse;
import com.example.aiwriter.config.AiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component("deepseekAiClient")
public class DeepSeekClient implements AiClient {

    private static final Logger log = LoggerFactory.getLogger(DeepSeekClient.class);

    private final RestTemplate restTemplate;

    private final AiProperties properties;

    public DeepSeekClient(RestTemplate restTemplate, AiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    @Retryable(
            retryFor = {RestClientException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public AiResponse generate(String prompt) {
        String url = properties.getDeepseek().getBaseUrl() + "/chat/completions";

        OpenAiRequest request = OpenAiRequest.builder()
                .model(properties.getDeepseek().getModel())
                .messages(Collections.singletonList(OpenAiRequest.Message.user(prompt)))
                .temperature(0.7)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + properties.getDeepseek().getApiKey());

        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<OpenAiResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    OpenAiResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                OpenAiResponse body = response.getBody();
                if (body.getChoices() != null && !body.getChoices().isEmpty()) {
                    String content = body.getChoices().get(0).getMessage().getContent();
                    Integer tokenUsage = body.getUsage() != null ? body.getUsage().getTotal_tokens() : null;
                    log.info("DeepSeek request successful, tokens used: {}", tokenUsage);
                    return AiResponse.success(content, tokenUsage);
                }
            }
            log.warn("DeepSeek response empty or invalid: {}", response.getStatusCode());
            return AiResponse.failure("Empty or invalid response from DeepSeek");
        } catch (RestClientException e) {
            log.error("DeepSeek API call failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String getProviderName() {
        return "deepseek";
    }
}
