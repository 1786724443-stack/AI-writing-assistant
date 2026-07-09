package com.example.aiwriter.client;

import com.example.aiwriter.config.AiProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AiClientFactory {

    private final Map<String, AiClient> clientMap;

    private final AiProperties properties;

    public AiClientFactory(Map<String, AiClient> clientMap, AiProperties properties) {
        this.clientMap = clientMap;
        this.properties = properties;
    }

    public AiClient getClient() {
        String type = properties.getType().toLowerCase();
        AiClient client = clientMap.get(type + "AiClient");
        if (client == null) {
            throw new IllegalArgumentException("Unknown AI provider: " + type);
        }
        return client;
    }

    public AiClient getClient(String type) {
        AiClient client = clientMap.get(type.toLowerCase() + "AiClient");
        if (client == null) {
            throw new IllegalArgumentException("Unknown AI provider: " + type);
        }
        return client;
    }
}
