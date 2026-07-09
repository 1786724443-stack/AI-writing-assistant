package com.example.aiwriter.config;

import com.example.aiwriter.interceptor.JwtInterceptor;
import com.example.aiwriter.interceptor.QuotaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final QuotaInterceptor quotaInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor, QuotaInterceptor quotaInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.quotaInterceptor = quotaInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/generate", "/api/polish", "/api/expand", "/api/summarize", "/api/translate", "/api/history", "/api/quota")
                .excludePathPatterns("/api/auth/**", "/api/health", "/swagger-ui/**", "/api-docs/**", "/h2-console/**", "/", "/index.html", "/static/**");

        registry.addInterceptor(quotaInterceptor)
                .addPathPatterns("/api/generate", "/api/polish", "/api/expand", "/api/summarize", "/api/translate")
                .excludePathPatterns("/api/auth/**", "/api/health", "/swagger-ui/**", "/api-docs/**", "/h2-console/**", "/", "/index.html", "/static/**");
    }
}
