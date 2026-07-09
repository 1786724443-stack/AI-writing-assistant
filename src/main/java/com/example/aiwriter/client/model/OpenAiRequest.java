package com.example.aiwriter.client.model;

import java.util.List;

public class OpenAiRequest {

    private String model;

    private List<Message> messages;

    private Double temperature = 0.7;

    public OpenAiRequest() {
    }

    public OpenAiRequest(String model, List<Message> messages, Double temperature) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public static OpenAiRequestBuilder builder() {
        return new OpenAiRequestBuilder();
    }

    public static class OpenAiRequestBuilder {
        private String model;
        private List<Message> messages;
        private Double temperature = 0.7;

        public OpenAiRequestBuilder model(String model) {
            this.model = model;
            return this;
        }

        public OpenAiRequestBuilder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        public OpenAiRequestBuilder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public OpenAiRequest build() {
            return new OpenAiRequest(model, messages, temperature);
        }
    }

    public static class Message {
        private String role;
        private String content;

        public Message() {
        }

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public static MessageBuilder builder() {
            return new MessageBuilder();
        }

        public static MessageBuilder builder(String role, String content) {
            return new MessageBuilder().role(role).content(content);
        }

        public static Message user(String content) {
            return new Message("user", content);
        }

        public static Message system(String content) {
            return new Message("system", content);
        }

        public static class MessageBuilder {
            private String role;
            private String content;

            public MessageBuilder role(String role) {
                this.role = role;
                return this;
            }

            public MessageBuilder content(String content) {
                this.content = content;
                return this;
            }

            public Message build() {
                return new Message(role, content);
            }
        }
    }
}
