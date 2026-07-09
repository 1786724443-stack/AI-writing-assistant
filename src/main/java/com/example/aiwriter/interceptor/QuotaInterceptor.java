package com.example.aiwriter.interceptor;

import com.example.aiwriter.entity.User;
import com.example.aiwriter.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class QuotaInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    public QuotaInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return true;
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"用户不存在\", \"statusCode\": 401}");
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate lastResetDate = user.getQuotaResetDate() != null ? user.getQuotaResetDate().toLocalDate() : null;

        if (!today.equals(lastResetDate)) {
            user.setUsedQuota(0);
            user.setQuotaResetDate(LocalDateTime.now());
            userRepository.save(user);
        }

        if (user.getUsedQuota() >= user.getDailyQuota()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"QuotaExceeded\", \"message\": \"今日配额已用完，请明天再试\", \"statusCode\": 429}");
            return false;
        }

        return true;
    }
}
