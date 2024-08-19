package com.example.demo.controller;

import com.example.demo.service.OpenAIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {


    private final OpenAIService openAIService;

    public ChatController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

        @PostMapping("/chat")
        public String chat(@RequestParam String prompt) {
            String str= openAIService.getChatGPTResponse(prompt + "위 요약본을 토대로 문제를 단답형 10 문제와 답은 단어 하나로만 이루어져 있다. question과 answer으로 이루어진 JSON으로 만들어줘");
            String jsonResponse = str;
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                // JSON 문자열을 파싱
                JsonNode rootNode = objectMapper.readTree(jsonResponse);
                JsonNode choicesNode = rootNode.path("choices");
                JsonNode messageNode = choicesNode.get(0).path("message");
                String content = messageNode.path("content").asText();

                // 이중 문자열로 된 JSON을 파싱
                JsonNode contentNode = objectMapper.readTree(content);

                // 각 질문과 답변을 출력
                if (contentNode.isArray()) {
                    for (JsonNode node : contentNode) {
                        String question = node.path("question").asText();
                        String answer = node.path("answer").asText();
                        System.out.println("Question: " + question);
                        System.out.println("Answer: " + answer);
                        System.out.println();
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return str;
    }
}


