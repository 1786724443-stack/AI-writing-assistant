package com.example.aiwriter.client.model;

public class AiResponse {

    private String content;

    private Integer tokenUsage;

    private Boolean success;

    private String errorMessage;

    public AiResponse() {
    }

    public AiResponse(String content, Integer tokenUsage, Boolean success, String errorMessage) {
        this.content = content;
        this.tokenUsage = tokenUsage;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTokenUsage() {
        return tokenUsage;
    }

    public void setTokenUsage(Integer tokenUsage) {
        this.tokenUsage = tokenUsage;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static AiResponse success(String content, Integer tokenUsage) {
        return new AiResponse(content, tokenUsage, true, null);
    }

    public static AiResponse failure(String errorMessage) {
        return new AiResponse(null, null, false, errorMessage);
    }
}
