package com.example.aiwriter.controller;

import com.example.aiwriter.dto.request.*;
import com.example.aiwriter.dto.response.WritingResponse;
import com.example.aiwriter.service.WritingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "写作助手", description = "AI 文章处理相关接口")
public class WritingController {

    private static final Logger log = LoggerFactory.getLogger(WritingController.class);

    private final WritingService writingService;

    public WritingController(WritingService writingService) {
        this.writingService = writingService;
    }

    @PostMapping("/generate")
    @Operation(summary = "生成文章", description = "根据主题和风格要求生成一篇文章")
    public ResponseEntity<WritingResponse> generate(@Valid @RequestBody GenerateRequest request) {
        log.info("Received generate request: topic={}", request.getTopic());
        WritingResponse response = writingService.generate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/polish")
    @Operation(summary = "润色文章", description = "优化文章的语法、风格和表达")
    public ResponseEntity<WritingResponse> polish(@Valid @RequestBody PolishRequest request) {
        log.info("Received polish request: contentLength={}", request.getContent().length());
        WritingResponse response = writingService.polish(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/expand")
    @Operation(summary = "扩写文章", description = "将内容扩展至指定长度")
    public ResponseEntity<WritingResponse> expand(@Valid @RequestBody ExpandRequest request) {
        log.info("Received expand request: contentLength={}, targetLength={}", 
                request.getContent().length(), request.getTargetLength());
        WritingResponse response = writingService.expand(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/summarize")
    @Operation(summary = "摘要文章", description = "生成文章摘要")
    public ResponseEntity<WritingResponse> summarize(@Valid @RequestBody SummarizeRequest request) {
        log.info("Received summarize request: contentLength={}, maxLength={}", 
                request.getContent().length(), request.getMaxLength());
        WritingResponse response = writingService.summarize(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/translate")
    @Operation(summary = "翻译文章", description = "将文章翻译成指定语言")
    public ResponseEntity<WritingResponse> translate(@Valid @RequestBody TranslateRequest request) {
        log.info("Received translate request: contentLength={}, targetLanguage={}", 
                request.getContent().length(), request.getTargetLanguage());
        WritingResponse response = writingService.translate(request);
        return ResponseEntity.ok(response);
    }
}
