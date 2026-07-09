package com.example.aiwriter.controller;

import com.example.aiwriter.dto.response.ErrorResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            log.warn("Error occurred: {} {} - {}", statusCode, requestUri, message);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ErrorResponse.of("Not Found", "请求的资源不存在: " + requestUri, HttpStatus.NOT_FOUND.value())
                );
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ErrorResponse.of("Unauthorized", "未授权访问，请登录后重试", HttpStatus.UNAUTHORIZED.value())
                );
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ErrorResponse.of("Forbidden", "访问被拒绝", HttpStatus.FORBIDDEN.value())
                );
            } else if (statusCode == HttpStatus.TOO_MANY_REQUESTS.value()) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                    ErrorResponse.of("Too Many Requests", "请求过于频繁，请稍后重试", HttpStatus.TOO_MANY_REQUESTS.value())
                );
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse.of("Internal Server Error", "服务器内部错误，请稍后重试", HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }
}
