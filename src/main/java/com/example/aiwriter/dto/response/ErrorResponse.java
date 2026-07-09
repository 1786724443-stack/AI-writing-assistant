package com.example.aiwriter.dto.response;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String error;

    private String message;

    private Integer statusCode;

    private LocalDateTime timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(String error, String message, Integer statusCode, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public static ErrorResponse of(String error, String message, Integer statusCode) {
        return new ErrorResponse(error, message, statusCode, LocalDateTime.now());
    }

    public static class ErrorResponseBuilder {
        private String error;
        private String message;
        private Integer statusCode;
        private LocalDateTime timestamp;

        public ErrorResponseBuilder error(String error) {
            this.error = error;
            return this;
        }

        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ErrorResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(error, message, statusCode, timestamp);
        }
    }
}
