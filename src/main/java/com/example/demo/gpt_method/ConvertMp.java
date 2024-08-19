package com.example.demo.gpt_method;


import java.io.File;
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

public class ConvertMp {

    private static final String API_KEY = "sk-Qj0JvbXL0ZeNKc7yzs0OK9zUO9F6NU8msdNLZ3gkKyT3BlbkFJdhBpeNfDKY346JM86Scb8oEPsyeC4hG5VM-dsCWNQA"; // OpenAI API 키 설정
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String c_mp(String mp) {
        try {
            // 1. PDF 파일을 텍스트로 변환


            // 2. 텍스트를 ChatGPT API로 전송하여 요약 요청
            String summary = summarizeTextWithChatGpt(mp);

            // 3. 요약 결과 반환
            return summary;

        } catch (IOException e) {
            e.printStackTrace();
            return "요약 불가능";
        }
    }

    private static String extractTextFromPdf(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setSortByPosition(true);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(document.getNumberOfPages());
            return pdfStripper.getText(document);
        }
    }

    private static String summarizeTextWithChatGpt(String text) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL);

        // 요청 헤더 설정
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setHeader("Authorization", "Bearer " + API_KEY);

        // 요청 메시지 생성
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant that summarizes text. 결과를 한글로 해줘.");

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", "Please summarize the following text:\n\n" + text);

        Map<String, Object> json = new HashMap<>();
        json.put("model", "gpt-4");
        json.put("messages", new Object[]{systemMessage, userMessage});

        StringEntity entity = new StringEntity(new Gson().toJson(json), "UTF-8");
        httpPost.setEntity(entity);

        // 요청 전송 및 응답 수신
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String result = EntityUtils.toString(responseEntity, "UTF-8");

        // 연결 종료
        httpClient.close();

        // JSON 응답 파싱하여 요약 내용 추출
        JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
        return jsonObject.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
    }
}