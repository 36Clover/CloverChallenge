package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class OpenAIService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.api-url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;  // 사용할 모델명을 설정하는 속성

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenAIService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getChatGPTResponse(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String sanitizedPrompt = sanitizePrompt(prompt);

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(new ChatRequest(model, sanitizedPrompt));
        } catch (JsonProcessingException e) {
            return "Error serializing request: " + e.getMessage();
        }

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            return "Error: " + e.getResponseBodyAsString();
        }
    }

    private String sanitizePrompt(String prompt) {
        if (prompt == null) return "";
        return prompt.trim().replaceAll("[\\r\\n]+", " ").replaceAll("\\s+", " ");
    }

    private static class ChatRequest {
        private String model;
        private Message[] messages;
        private int max_tokens = 2000;

        public ChatRequest(String model, String prompt) {
            this.model = model;
            this.messages = new Message[]{ new Message("user", prompt) };
        }

        // Getters and Setters
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public Message[] getMessages() { return messages; }
        public void setMessages(Message[] messages) { this.messages = messages; }
        public int getMax_tokens() { return max_tokens; }
        public void setMax_tokens(int max_tokens) { this.max_tokens = max_tokens; }
    }

    private static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        // Getters and Setters
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}

//http://localhost:8080/chat?prompt={질문}
