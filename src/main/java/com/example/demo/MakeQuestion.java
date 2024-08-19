package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MakeQuestion {

    // OpenAI API 키와 엔드포인트 URL 설정
    private static final String API_KEY = "sk-Qj0JvbXL0ZeNKc7yzs0OK9zUO9F6NU8msdNLZ3gkKyT3BlbkFJdhBpeNfDKY346JM86Scb8oEPsyeC4hG5VM-dsCWNQA";  // 실제 API 키로 교체
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String make_question(String str) {
        String answer = null;
        try {

            // 처리할 텍스트 정의
            String textToProcess = str +
                    "위 요약본을 토대로 문제를 단답형 10 문제와 답은 단어 하나로만 이루어져 있다. question과 answer으로 이루어진 JSON으로 만들어줘";

            // 텍스트를 처리하여 답변을 추출
            answer = getAnswer(textToProcess);


        } catch (Exception e) {
            e.printStackTrace();  // 예외 발생 시 스택 트레이스를 출력합니다.
        }

        return answer;
    }

    // 텍스트를 처리하여 답변을 얻는 메소드
    private static String getAnswer(String text) throws Exception {
        // 답변을 얻기 위한 프롬프트 작성
        String prompt = "Provide an answer based on the following information:\n" + text;
        return callOpenAI(prompt);  // OpenAI API 호출
    }

    // OpenAI API를 호출하여 응답을 받는 메소드
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
            // 응답에서 답변 추출
            return responseJson.path("choices").get(0).path("message").path("content").asText().trim();
        }
    }
}
