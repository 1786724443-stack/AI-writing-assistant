package com.example.aiwriter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "文章扩写请求")
public class ExpandRequest {

    @NotBlank(message = "内容不能为空")
    @Size(max = 50000, message = "内容长度不能超过50000个字符")
    @Schema(description = "需要扩写的文章内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Min(value = 500, message = "目标长度不能少于500字")
    @Max(value = 20000, message = "目标长度不能超过20000字")
    @Schema(description = "扩写目标字数", example = "1500", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer targetLength;

    public ExpandRequest() {
    }

    public ExpandRequest(String content, Integer targetLength) {
        this.content = content;
        this.targetLength = targetLength;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTargetLength() {
        return targetLength;
    }

    public void setTargetLength(Integer targetLength) {
        this.targetLength = targetLength;
    }
}
