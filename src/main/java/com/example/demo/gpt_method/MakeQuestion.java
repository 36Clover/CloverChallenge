package com.example.demo.gpt_method;

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

    /**
     * 주어진 요약을 바탕으로 단답형 문제를 생성하는 메소드입니다.
     * @param str 요약 정보
     * @return 질문과 답변의 리스트
     */
    public List<Map<String, String>> make_question(String str) {
        List<Map<String, String>> questions = new ArrayList<>();
        try {
            // 처리할 텍스트 정의: 요약 텍스트와 문제 생성을 위한 지시문
            String textToProcess = str +
                    "위 요약본을 토대로 문제를 단답형 5 문제와 답은 단어 하나로만 이루어져 있다. question과 answer으로 이루어진 JSON으로 만들어줘";

            // 텍스트를 처리하여 답변을 추출
            String jsonResponse = getAnswer(textToProcess);

            // JSON 응답을 파싱하여 질문과 답변 리스트에 추가
            questions = parseQuestions(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();  // 예외 발생 시 스택 트레이스를 출력합니다.
        }

        return questions;
    }

    // 추출한 문제들을 텍스트 파일로 저장하는 메소드
    private void saveQuestionsToFile(String jsonContent, String fileName) {
        try {
            // JSON 문자열 파싱
            JsonNode contentNode = new ObjectMapper().readTree(jsonContent);

            // 텍스트 파일 작성
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                for (JsonNode node : contentNode) {
                    String question = node.path("question").asText();
                    String answer = node.path("answer").asText();
                    fileWriter.write("Question: " + question + "\n");
                    fileWriter.write("Answer: " + answer + "\n\n");
                }
            }
            System.out.println("Questions and answers have been written to " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 텍스트를 기반으로 OpenAI API를 호출하여 답변을 얻는 메소드입니다.
     * @param text 요청할 텍스트
     * @return API 응답으로부터 얻은 JSON 응답 문자열
     * @throws Exception API 호출 중 발생할 수 있는 예외
     */
    private static String getAnswer(String text) throws Exception {
        // 답변을 얻기 위한 프롬프트 작성
        String prompt = "Provide an answer based on the following information:\n" + text;
        return callOpenAI(prompt);  // OpenAI API 호출
    }


    /**
     * OpenAI API를 호출하여 응답을 받는 메소드입니다.
     * @param prompt 요청할 프롬프트
     * @return API 응답 문자열
     * @throws Exception API 호출 중 발생할 수 있는 예외
     */
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
            // 응답 JSON 문자열 반환
            return response.toString();
        }
    }

    /**
     * JSON 응답을 파싱하여 질문과 답변 리스트를 생성하는 메소드입니다.
     * @param jsonResponse OpenAI API로부터 받은 JSON 응답
     * @return 질문과 답변의 리스트
     * @throws Exception JSON 파싱 중 발생할 수 있는 예외
     */
    private static List<Map<String, String>> parseQuestions(String jsonResponse) throws Exception {
        List<Map<String, String>> questionsList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        // 응답 JSON 파싱
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode choicesNode = rootNode.path("choices");

        if (choicesNode.isArray() && choicesNode.size() > 0) {
            JsonNode messageNode = choicesNode.get(0).path("message").path("content");

            // JSON 응답을 다시 파싱하여 질문과 답변 추출
            JsonNode questionsNode = objectMapper.readTree(messageNode.asText()).path("questions");

            for (JsonNode questionNode : questionsNode) {
                String question = questionNode.path("question").asText();
                String answer = questionNode.path("answer").asText();

                // 질문과 답변을 Map으로 생성 후 리스트에 추가
                questionsList.add(Map.of("question", question, "answer", answer));
            }
        }

        return questionsList;
    }
}
