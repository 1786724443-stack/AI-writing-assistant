package com.example.aiwriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class AiWriterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiWriterApplication.class, args);
    }
}
