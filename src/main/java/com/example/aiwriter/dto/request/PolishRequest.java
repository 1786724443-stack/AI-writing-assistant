package com.example.aiwriter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "文章润色请求")
public class PolishRequest {

    @NotBlank(message = "内容不能为空")
    @Size(max = 50000, message = "内容长度不能超过50000个字符")
    @Schema(description = "需要润色的文章内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Size(max = 500, message = "风格要求长度不能超过500个字符")
    @Schema(description = "润色风格要求", example = "正式、专业")
    private String style;

    public PolishRequest() {
    }

    public PolishRequest(String content, String style) {
        this.content = content;
        this.style = style;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
