package com.example.aiwriter.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "写作响应")
public class WritingResponse {

    @Schema(description = "请求ID")
    private String id;

    @Schema(description = "生成的文章内容")
    private String content;

    @Schema(description = "使用的Token数量")
    private Integer tokenCount;

    @Schema(description = "响应耗时(毫秒)")
    private Long duration;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    public WritingResponse() {
    }

    public WritingResponse(String id, String content, Integer tokenCount, Long duration, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.tokenCount = tokenCount;
        this.duration = duration;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static WritingResponseBuilder builder() {
        return new WritingResponseBuilder();
    }

    public static class WritingResponseBuilder {
        private String id;
        private String content;
        private Integer tokenCount;
        private Long duration;
        private LocalDateTime createdAt;

        public WritingResponseBuilder id(String id) {
            this.id = id;
            return this;
        }

        public WritingResponseBuilder content(String content) {
            this.content = content;
            return this;
        }

        public WritingResponseBuilder tokenCount(Integer tokenCount) {
            this.tokenCount = tokenCount;
            return this;
        }

        public WritingResponseBuilder duration(Long duration) {
            this.duration = duration;
            return this;
        }

        public WritingResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public WritingResponse build() {
            return new WritingResponse(id, content, tokenCount, duration, createdAt);
        }
    }
}
