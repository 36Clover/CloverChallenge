package com.example.demo.gpt_method;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Summary {

    // OpenAI API 키와 엔드포인트 URL 설정
    private static final String API_KEY = "sk-Qj0JvbXL0ZeNKc7yzs0OK9zUO9F6NU8msdNLZ3gkKyT3BlbkFJdhBpeNfDKY346JM86Scb8oEPsyeC4hG5VM-dsCWNQA";  // 실제 API 키로 교체
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String make_summary(String str) {
        String summary = null;
        try {
            // 요약할 텍스트
            String textToSummarize = str
                    + "\n을 요약해줘";

            // 텍스트를 요약하여 결과 출력
            summary = getSummary(textToSummarize);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return summary;
    }

    // 텍스트를 요약하는 메소드
    private static String getSummary(String text) throws Exception {
        // 프롬프트 작성
        String prompt = "Please summarize the following text:\n" + text;
        return callOpenAI(prompt);
    }

    // OpenAI API를 호출하여 요약 결과를 받는 메소드
    private static String callOpenAI(String prompt) throws Exception {
        // API URL과 연결 설정
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);  // API 키를 헤더에 설정
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");  // 요청 본문 타입 설정
        conn.setRequestProperty("Accept", "application/json");  // 응답 형식 설정
        conn.setDoOutput(true);  // 출력을 사용할 수 있도록 설정

        // JSON 요청 본문 생성
        String jsonRequest = "{"
                + "\"model\": \"gpt-4\","  // 사용할 모델 지정
                + "\"messages\": ["  // 메시지 배열 설정
                + "{\"role\": \"user\", \"content\": \"" + prompt.replaceAll("\n", "\\\\n").replaceAll("\"", "\\\\\"") + "\"}"  // 사용자 역할과 프롬프트 설정
                + "]"
                + "}";

        // 요청 본문을 서버에 전송
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonRequest.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        // 서버로부터 응답 읽기
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            // 응답 JSON 파싱
            JsonNode responseJson = new ObjectMapper().readTree(response.toString());
            // 응답에서 요약된 내용 추출
            return responseJson.path("choices").get(0).path("message").path("content").asText().trim();
        }
    }
}
