package com.example.aiwriter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "文章翻译请求")
public class TranslateRequest {

    @NotBlank(message = "内容不能为空")
    @Size(max = 50000, message = "内容长度不能超过50000个字符")
    @Schema(description = "需要翻译的文章内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @NotBlank(message = "目标语言不能为空")
    @Size(max = 100, message = "目标语言长度不能超过100个字符")
    @Schema(description = "目标语言", example = "中文", requiredMode = Schema.RequiredMode.REQUIRED)
    private String targetLanguage;

    public TranslateRequest() {
    }

    public TranslateRequest(String content, String targetLanguage) {
        this.content = content;
        this.targetLanguage = targetLanguage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }
}
