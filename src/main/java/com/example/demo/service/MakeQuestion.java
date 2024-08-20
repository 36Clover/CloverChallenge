package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MakeQuestion {

    // OpenAI API 키와 엔드포인트 URL 설정
    private static final String API_KEY = "sk-Qj0JvbXL0ZeNKc7yzs0OK9zUO9F6NU8msdNLZ3gkKyT3BlbkFJdhBpeNfDKY346JM86Scb8oEPsyeC4hG5VM-dsCWNQA";  // 실제 API 키로 교체
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public List<Map<String, String>> make_question(String str) {
        List<Map<String, String>> questions = new ArrayList<>();
        try {
            String textToProcess = str +
                    "위 요약본을 토대로 문제를 단답형 5 문제와 답은 단어 하나로만 이루어져 있다. question과 answer으로 이루어진 JSON으로 만들어줘";

            String jsonResponse = getAnswer(textToProcess);
            questions = parseQuestions(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }

    private static String getAnswer(String text) throws Exception {
        String prompt = text;
        return callOpenAI(prompt);
    }

    private static String callOpenAI(String prompt) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        String jsonRequest = "{"
                + "\"model\": \"gpt-4\","
                + "\"messages\": ["
                + "{\"role\": \"user\", \"content\": \"" + prompt.replace("\n", "\\n").replace("\"", "\\\"") + "\"}"
                + "]"
                + "}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonRequest.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    private static List<Map<String, String>> parseQuestions(String jsonResponse) throws Exception {
        List<Map<String, String>> questionsList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode choicesNode = rootNode.path("choices");

        if (choicesNode.isArray() && choicesNode.size() > 0) {
            JsonNode messageNode = choicesNode.get(0).path("message").path("content");
            JsonNode parsedContent = objectMapper.readTree(messageNode.asText());

            for (JsonNode questionNode : parsedContent) {
                String question = questionNode.path("question").asText();
                String answer = questionNode.path("answer").asText();
                questionsList.add(Map.of("question", question, "answer", answer));
            }
        }

        return questionsList;
    }
}