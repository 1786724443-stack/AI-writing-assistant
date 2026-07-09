package com.example.aiwriter.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "配额响应")
public class QuotaResponse {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "每日配额")
    private Integer dailyQuota;

    @Schema(description = "已使用配额")
    private Integer usedQuota;

    @Schema(description = "剩余配额")
    private Integer remainingQuota;

    @Schema(description = "配额重置时间")
    private LocalDateTime quotaResetDate;

    public QuotaResponse() {
    }

    public QuotaResponse(String username, Integer dailyQuota, Integer usedQuota, 
                         Integer remainingQuota, LocalDateTime quotaResetDate) {
        this.username = username;
        this.dailyQuota = dailyQuota;
        this.usedQuota = usedQuota;
        this.remainingQuota = remainingQuota;
        this.quotaResetDate = quotaResetDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getDailyQuota() {
        return dailyQuota;
    }

    public void setDailyQuota(Integer dailyQuota) {
        this.dailyQuota = dailyQuota;
    }

    public Integer getUsedQuota() {
        return usedQuota;
    }

    public void setUsedQuota(Integer usedQuota) {
        this.usedQuota = usedQuota;
    }

    public Integer getRemainingQuota() {
        return remainingQuota;
    }

    public void setRemainingQuota(Integer remainingQuota) {
        this.remainingQuota = remainingQuota;
    }

    public LocalDateTime getQuotaResetDate() {
        return quotaResetDate;
    }

    public void setQuotaResetDate(LocalDateTime quotaResetDate) {
        this.quotaResetDate = quotaResetDate;
    }
}
