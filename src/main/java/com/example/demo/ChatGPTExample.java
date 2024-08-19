package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPTExample {

    // OpenAI API 키와 엔드포인트 URL 설정
    private static final String API_KEY = "sk-Qj0JvbXL0ZeNKc7yzs0OK9zUO9F6NU8msdNLZ3gkKyT3BlbkFJdhBpeNfDKY346JM86Scb8oEPsyeC4hG5VM-dsCWNQA";  // 실제 API 키로 교체
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) {
        try {

            String str="OSI 7계층 구조는 네트워크 통신에서 데이터를 전송하는 과정을 설명하는 표준 모델입니다. OSI(Open Systems Interconnection) 모델은 통신 시스템의 상호운용성을 높이기 위해 설계되었습니다. 이 모델은 네트워크 통신을 7개의 계층으로 나누어 각 계층이 특정 기능을 수행하도록 정의합니다. 각 계층은 상위 계층에 서비스를 제공하며 하위 계층에 서비스를 요청합니다.\n" +
                    "OSI 7계층\n" +
                    "물리 계층 (Physical Layer)\n" +
                    "기능: 데이터 비트의 전기적 신호나 광 신호로의 변환과 전송을 담당합니다.\n" +
                    "주요 요소: 케이블, 커넥터, 네트워크 카드, 전송 매체 (예: Ethernet, USB).\n" +
                    "데이터 단위: 비트 (bit).\n" +
                    "데이터 링크 계층 (Data Link Layer)\n" +
                    "기능: 물리 계층에서 전송된 데이터의 오류 검출 및 수정, 데이터 프레임의 형성, MAC 주소를 사용하여 장치 간의 통신을 담당합니다.\n" +
                    "주요 요소: 스위치, 브리지, NIC (Network Interface Card).\n" +
                    "데이터 단위: 프레임 (frame).\n" +
                    "네트워크 계층 (Network Layer)\n" +
                    "기능: 데이터 패킷을 목적지까지 전송하며, 라우팅과 패킷 스위칭을 담당합니다. IP 주소를 사용하여 네트워크 간의 경로를 결정합니다.\n" +
                    "주요 요소: 라우터, IP 주소.\n" +
                    "데이터 단위: 패킷 (packet).\n" +
                    "전송 계층 (Transport Layer)\n" +
                    "기능: 데이터의 전송을 보장하며, 오류 제어 및 흐름 제어를 수행합니다. 신뢰성 있는 전송을 위해 세그먼트 단위로 나누어 전송합니다.\n" +
                    "주요 요소: TCP (Transmission Control Protocol), UDP (User Datagram Protocol).\n" +
                    "데이터 단위: 세그먼트 (segment) / 데이터그램 (datagram).\n" +
                    "세션 계층 (Session Layer)\n" +
                    "기능: 응용 프로그램 간의 세션을 설정, 관리, 종료하며, 통신 세션의 유지와 동기화를 담당합니다.\n" +
                    "주요 요소: 세션 관리, 동기화, 체크포인트.\n" +
                    "데이터 단위: 데이터.\n" +
                    "표현 계층 (Presentation Layer)\n" +
                    "기능: 데이터의 형식을 변환하고, 데이터 압축 및 암호화를 수행하여 응용 프로그램이 이해할 수 있는 형식으로 변환합니다.\n" +
                    "주요 요소: 데이터 변환, 압축, 암호화.\n" +
                    "데이터 단위: 데이터.\n" +
                    "응용 계층 (Application Layer)\n" +
                    "기능: 사용자와 직접 상호작용하며, 응용 프로그램이 네트워크 서비스를 사용할 수 있도록 지원합니다. 다양한 네트워크 서비스와 프로토콜을 제공합니다.\n" +
                    "주요 요소: 웹 브라우저, 이메일 클라이언트, FTP, HTTP, DNS.\n";

            // 처리할 텍스트 정의
            String textToProcess = str+
                    "위 요약본을 토대로 문제를 단답형 10 문제와 답은 단어 하나로만 이루어져 있다. question과 answer으로 이루어진 JSON으로 만들어줘";

            // 텍스트를 처리하여 답변을 추출
            String answer = getAnswer(textToProcess);
            System.out.println("Answer: " + answer);

        } catch (Exception e) {
            e.printStackTrace();  // 예외 발생 시 스택 트레이스를 출력합니다.
        }
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
