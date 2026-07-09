package com.example.aiwriter.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "认证响应")
public class AuthResponse {

    @Schema(description = "JWT令牌")
    private String token;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "剩余配额")
    private Integer remainingQuota;

    public AuthResponse() {
    }

    public AuthResponse(String token, String username, Integer remainingQuota) {
        this.token = token;
        this.username = username;
        this.remainingQuota = remainingQuota;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRemainingQuota() {
        return remainingQuota;
    }

    public void setRemainingQuota(Integer remainingQuota) {
        this.remainingQuota = remainingQuota;
    }
}
