package com.example.aiwriter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "文章摘要请求")
public class SummarizeRequest {

    @NotBlank(message = "内容不能为空")
    @Size(max = 50000, message = "内容长度不能超过50000个字符")
    @Schema(description = "需要生成摘要的文章内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Min(value = 50, message = "摘要长度不能少于50字")
    @Max(value = 2000, message = "摘要长度不能超过2000字")
    @Schema(description = "摘要最大字数", example = "300", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer maxLength;

    public SummarizeRequest() {
    }

    public SummarizeRequest(String content, Integer maxLength) {
        this.content = content;
        this.maxLength = maxLength;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
