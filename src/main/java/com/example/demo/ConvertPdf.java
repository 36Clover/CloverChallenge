package com.example.demo;

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

public class ConvertPdf {

    private static final String API_KEY = "sk-Qj0JvbXL0ZeNKc7yzs0OK9zUO9F6NU8msdNLZ3gkKyT3BlbkFJdhBpeNfDKY346JM86Scb8oEPsyeC4hG5VM-dsCWNQA"; // OpenAI API 키 설정
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String c_pdf() {
        try {
            // 1. PDF 파일을 텍스트로 변환
            String pdfText = extractTextFromPdf("C:\\Users\\mdk19\\Desktop\\testpdf.pdf");

            // 2. 텍스트를 ChatGPT API로 전송하여 요약 요청
            String summary = summarizeTextWithChatGpt(pdfText);

            // 3. 요약 결과 출력
            System.out.println("Summary:");
            System.out.println(summary);
            return  summary;

        } catch (IOException e) {
            e.printStackTrace();
            return "요약불가능";
        }
        
    }

    private static String extractTextFromPdf(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setSortByPosition(true); // 텍스트 위치 정렬
            pdfStripper.setStartPage(1); // 시작 페이지 (필요에 따라 변경 가능)
            pdfStripper.setEndPage(document.getNumberOfPages()); // 마지막 페이지
            return pdfStripper.getText(document);
        }
    }

    private static String summarizeTextWithChatGpt(String text) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL);

        // 2.1 요청 헤더 설정
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setHeader("Authorization", "Bearer " + API_KEY);

        // 2.2 시스템 메시지 및 사용자 메시지 생성
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant that summarizes text 결과를 한글로해줘.");

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", "Please summarize the following text:\n\n" + text);

        Map<String, Object> json = new HashMap<>();
        json.put("model", "gpt-4");  // 사용하는 모델 선택
        json.put("messages", new Object[]{systemMessage, userMessage});

        StringEntity entity = new StringEntity(new Gson().toJson(json), "UTF-8");
        httpPost.setEntity(entity);

        // 2.3 요청 전송 및 응답 수신
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String result = EntityUtils.toString(responseEntity, "UTF-8");

        // 2.4 연결 종료
        httpClient.close();

        // 2.5 JSON 응답 파싱하여 요약 내용 추출
        JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
        return jsonObject.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
    }
}