package com.example.aiwriter.exception;

import com.example.aiwriter.dto.response.ErrorResponse;
import com.example.aiwriter.config.AiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final AiProperties properties;

    public GlobalExceptionHandler(AiProperties properties) {
        this.properties = properties;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation Failed");
        response.put("message", "请求参数验证失败");
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());
        response.put("details", errors);

        log.warn("Validation failed: {}", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex) {
        log.error("AI API authentication failed: {}", ex.getMessage());
        
        String provider = properties.getType().toUpperCase();
        String apiKeyConfig = properties.getType().equals("deepseek") 
                ? "DEEPSEEK_API_KEY" 
                : "OPENAI_API_KEY";
        
        ErrorResponse response = ErrorResponse.of(
                "Authentication Failed",
                "AI服务认证失败，请检查API密钥是否正确。当前提供商：" + provider + "，请确保环境变量 " + apiKeyConfig + " 已正确设置。",
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.error("AI API client error: {} - {}", ex.getStatusCode(), ex.getMessage());
        ErrorResponse response = ErrorResponse.of(
                "AI Client Error",
                "AI服务请求错误: " + ex.getMessage(),
                ex.getStatusCode().value()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpServerErrorException(HttpServerErrorException ex) {
        log.error("AI API server error: {} - {}", ex.getStatusCode(), ex.getMessage());
        ErrorResponse response = ErrorResponse.of(
                "AI Service Error",
                "AI服务端错误(" + ex.getStatusCode() + ")，请稍后重试",
                ex.getStatusCode().value()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException ex) {
        log.error("AI API connection failed: {}", ex.getMessage());
        
        String provider = properties.getType().toUpperCase();
        String baseUrl = properties.getType().equals("deepseek") 
                ? properties.getDeepseek().getBaseUrl() 
                : properties.getOpenai().getBaseUrl();
        
        ErrorResponse response = ErrorResponse.of(
                "Connection Failed",
                "无法连接到AI服务: " + baseUrl + "。请检查网络连接或API地址配置是否正确。当前提供商：" + provider,
                HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException ex) {
        log.error("AI API call failed: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse.of(
                "AI Service Error",
                "AI服务调用失败，请稍后重试",
                HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        log.warn("Runtime exception: {}", message);
        
        // 认证相关异常返回400或401状态码
        if (message.contains("用户名已存在") || message.contains("邮箱已被注册")) {
            return ResponseEntity.badRequest().body(
                ErrorResponse.of("Bad Request", message, HttpStatus.BAD_REQUEST.value())
            );
        }
        if (message.contains("用户不存在") || message.contains("密码错误")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.of("Unauthorized", message, HttpStatus.UNAUTHORIZED.value())
            );
        }
        if (message.contains("配额不足") || message.contains("超出每日配额")) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                ErrorResponse.of("Too Many Requests", message, HttpStatus.TOO_MANY_REQUESTS.value())
            );
        }
        
        // 其他异常返回500
        log.error("Unexpected error occurred: {}", message, ex);
        ErrorResponse response = ErrorResponse.of(
                "Internal Server Error",
                "服务器内部错误，请稍后重试",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse.of(
                "Bad Request",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(response);
    }
}
