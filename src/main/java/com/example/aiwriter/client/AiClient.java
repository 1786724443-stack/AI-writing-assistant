package com.example.aiwriter.client;

import com.example.aiwriter.client.model.AiResponse;

public interface AiClient {

    AiResponse generate(String prompt);

    String getProviderName();
}
