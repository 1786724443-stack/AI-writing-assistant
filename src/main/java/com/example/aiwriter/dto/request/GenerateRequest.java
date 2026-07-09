package com.example.aiwriter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "文章生成请求")
public class GenerateRequest {

    @NotBlank(message = "主题不能为空")
    @Size(max = 500, message = "主题长度不能超过500个字符")
    @Schema(description = "文章主题", example = "人工智能对未来教育的影响", requiredMode = Schema.RequiredMode.REQUIRED)
    private String topic;

    @Size(max = 1000, message = "风格要求长度不能超过1000个字符")
    @Schema(description = "文章风格要求", example = "客观、分析性强、面向教育从业者")
    private String style;

    public GenerateRequest() {
    }

    public GenerateRequest(String topic, String style) {
        this.topic = topic;
        this.style = style;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
