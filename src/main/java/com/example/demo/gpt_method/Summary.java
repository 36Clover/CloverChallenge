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

    public static void main(String[] args) {
        try {
            // 요약할 텍스트
            String textToSummarize = "네트워크의 7계층 모델은 OSI(Open Systems Interconnection) 모델을 말합니다. 이 모델은 컴퓨터 네트워킹의 구조를 설명하기 위해 설계된 7개의 계층으로 나뉩니다. 각 계층은 네트워크 통신의 특정 기능을 담당하며, 계층 간의 상호작용을 통해 데이터가 네트워크를 통해 전달됩니다. 각 계층은 다음과 같습니다:\n" +
                    "\n" +
                    "물리 계층 (Physical Layer): 전기적, 기계적, 절차적, 기능적 특성을 정의하여 데이터의 물리적 전송을 담당합니다. 예를 들어, 케이블, 스위치, 허브 등의 장비가 여기에 해당합니다.\n" +
                    "\n" +
                    "데이터 링크 계층 (Data Link Layer): 물리적 연결을 통해 데이터를 패킷으로 묶어 오류 검출 및 수정, 흐름 제어를 수행합니다. MAC 주소와 스위칭, 프레임 전송 등이 여기에 포함됩니다.\n" +
                    "\n" +
                    "네트워크 계층 (Network Layer): 데이터 패킷을 목적지까지 전달하는 경로를 설정하고 라우팅을 수행합니다. IP 주소와 라우터가 여기에 해당하며, 패킷 스위칭과 같은 기능을 담당합니다.\n" +
                    "\n" +
                    "전송 계층 (Transport Layer): 데이터 전송의 신뢰성을 보장합니다. 연결 지향적인 TCP와 연결 없는 UDP 프로토콜이 여기에 포함되며, 데이터 전송의 오류 복구와 흐름 제어를 담당합니다.\n" +
                    "\n" +
                    "세션 계층 (Session Layer): 네트워크 상에서 세션을 설정, 관리, 종료하는 역할을 합니다. 예를 들어, 데이터의 통신 세션을 설정하고 유지하며, 중단 후 다시 시작할 수 있도록 지원합니다.\n" +
                    "\n" +
                    "표현 계층 (Presentation Layer): 데이터의 형식을 변환하거나 인코딩, 디코딩을 수행하여 서로 다른 시스템 간의 데이터 표현 방식을 맞추는 역할을 합니다. 예를 들어, 암호화, 압축, 데이터 포맷 변환이 여기에 포함됩니다.\n" +
                    "\n" +
                    "응용 계층 (Application Layer): 최종 사용자와 가장 가까운 계층으로, 네트워크 서비스를 애플리케이션에 제공하고 응용 프로그램이 네트워크와 상호작용할 수 있도록 합니다. HTTP, FTP, SMTP와 같은 프로토콜이 여기에 해당합니다."
                    +"\n을 요약해줘";

            // 텍스트를 요약하여 결과 출력
            String summary = getSummary(textToSummarize);
            System.out.println("Summary: " + summary);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
