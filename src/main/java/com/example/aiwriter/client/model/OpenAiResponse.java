package com.example.aiwriter.client.model;

import java.util.List;

public class OpenAiResponse {

    private List<Choice> choices;

    private Usage usage;

    public OpenAiResponse() {
    }

    public OpenAiResponse(List<Choice> choices, Usage usage) {
        this.choices = choices;
        this.usage = usage;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public static class Choice {
        private Message message;
        private String finish_reason;
        private Integer index;

        public Choice() {
        }

        public Choice(Message message, String finish_reason, Integer index) {
            this.message = message;
            this.finish_reason = finish_reason;
            this.index = index;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public String getFinish_reason() {
            return finish_reason;
        }

        public void setFinish_reason(String finish_reason) {
            this.finish_reason = finish_reason;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }
    }

    public static class Usage {
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;

        public Usage() {
        }

        public Usage(Integer prompt_tokens, Integer completion_tokens, Integer total_tokens) {
            this.prompt_tokens = prompt_tokens;
            this.completion_tokens = completion_tokens;
            this.total_tokens = total_tokens;
        }

        public Integer getPrompt_tokens() {
            return prompt_tokens;
        }

        public void setPrompt_tokens(Integer prompt_tokens) {
            this.prompt_tokens = prompt_tokens;
        }

        public Integer getCompletion_tokens() {
            return completion_tokens;
        }

        public void setCompletion_tokens(Integer completion_tokens) {
            this.completion_tokens = completion_tokens;
        }

        public Integer getTotal_tokens() {
            return total_tokens;
        }

        public void setTotal_tokens(Integer total_tokens) {
            this.total_tokens = total_tokens;
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
    }
}
