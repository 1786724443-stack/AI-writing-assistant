package com.example.aiwriter.controller;

import com.example.aiwriter.dto.response.HistoryResponse;
import com.example.aiwriter.dto.response.QuotaResponse;
import com.example.aiwriter.entity.ApiCallLog;
import com.example.aiwriter.entity.User;
import com.example.aiwriter.repository.ApiCallLogRepository;
import com.example.aiwriter.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "用户", description = "用户配额和历史记录")
public class UserController {

    private final UserRepository userRepository;
    private final ApiCallLogRepository apiCallLogRepository;

    public UserController(UserRepository userRepository, ApiCallLogRepository apiCallLogRepository) {
        this.userRepository = userRepository;
        this.apiCallLogRepository = apiCallLogRepository;
    }

    @GetMapping("/quota")
    @Operation(summary = "查询配额", description = "查询当前用户的配额信息")
    public ResponseEntity<QuotaResponse> getQuota(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        QuotaResponse response = new QuotaResponse(
                user.getUsername(),
                user.getDailyQuota(),
                user.getUsedQuota(),
                user.getRemainingQuota(),
                user.getQuotaResetDate()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @Operation(summary = "查询历史记录", description = "查询用户的API调用历史记录")
    public ResponseEntity<Page<HistoryResponse>> getHistory(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = (Long) request.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size);

        Page<ApiCallLog> logs = apiCallLogRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        Page<HistoryResponse> response = logs.map(log -> {
            HistoryResponse hr = new HistoryResponse();
            hr.setId(log.getId());
            hr.setRequestId(log.getRequestId());
            hr.setEndpoint(log.getEndpoint());
            hr.setContent(log.getContent());
            hr.setTokenCount(log.getTokenCount());
            hr.setDurationMs(log.getDurationMs());
            hr.setStatus(log.getStatus());
            hr.setCreatedAt(log.getCreatedAt());
            return hr;
        });

        return ResponseEntity.ok(response);
    }
}
