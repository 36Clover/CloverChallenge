package com.example.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.google.gson.Gson;

public class testgpt2 {

    private static final String API_KEY = "sk-Qj0JvbXL0ZeNKc7yzs0OK9zUO9F6NU8msdNLZ3gkKyT3BlbkFJdhBpeNfDKY346JM86Scb8oEPsyeC4hG5VM-dsCWNQA"; // OpenAI API 키 설정
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) {
        String text = "안녕하세요, GPT-4를 사용하여 점자로 변환합니다.";

        try {
            // 1. 텍스트를 점자로 변환하도록 GPT API에 요청
            String brailleText = convertTextToBraille(text);

            // 2. 변환된 점자 출력
            System.out.println("Original Text: " + text);
            System.out.println("Braille Text: " + brailleText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertTextToBraille(String text) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL);

        // 1.1 요청 헤더 설정
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setHeader("Authorization", "Bearer " + API_KEY);

        // 1.2 메시지 생성
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant that converts text to Korean Braille.");

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", "Convert the following text to Braille:\n" + text);

        Map<String, Object> json = new HashMap<>();
        json.put("model", "gpt-4");  // 사용하는 모델 선택
        json.put("messages", new Object[]{systemMessage, userMessage});

        StringEntity entity = new StringEntity(new Gson().toJson(json), "UTF-8");
        httpPost.setEntity(entity);

        // 1.3 요청 전송 및 응답 수신
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String result = EntityUtils.toString(responseEntity, "UTF-8");

        // 1.4 연결 종료
        httpClient.close();

        // 1.5 JSON 응답 파싱하여 점자 텍스트 추출
        JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
        return jsonObject.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
    }
}