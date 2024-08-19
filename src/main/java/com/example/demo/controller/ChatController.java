package com.example.demo.controller;

import com.example.demo.ClovaSpeechClient;
import com.example.demo.dto.QADto;
import com.example.demo.gpt_method.MakeQuestion;
import com.example.demo.service.OpenAIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChatController {


    private final OpenAIService openAIService;

    public ChatController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }


    @PostMapping("/file")
    public String handleFileUpload(@RequestParam("recording") MultipartFile file) {
        try {
            // 파일 저장
            String uploadDir = "C:\\Users\\hyoju\\Desktop\\testpath\\";
            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath)); // 파일 저장

            // pa_json 메서드 호출
            String result = pa_json(filePath);

            // JSON 파싱 및 화자별 인식 결과 추출
            Map<String, Object> resultMap = new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
            }.getType());
            List<Map<String, Object>> segments = (List<Map<String, Object>>) resultMap.get("segments");
            List<Map<String, String>> speakerSegments = new ArrayList<>();

            if (segments != null) {
                for (Map<String, Object> segment : segments) {
                    Map<String, String> speaker = (Map<String, String>) segment.get("speaker");
                    String text = (String) segment.get("text");
                    speakerSegments.add(Map.of("speaker", "", "text", text));
                }
            }

            String combinedText = speakerSegments.stream()
                    .map(segment -> segment.get("text"))
                    .collect(Collectors.joining());

            String prompt = combinedText;

            MakeQuestion makeQuestion = new MakeQuestion();
            makeQuestion.make_question(prompt);

            return "/result";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String pa_json(String filePath) {
        final ClovaSpeechClient clovaSpeechClient = new ClovaSpeechClient();
        ClovaSpeechClient.NestRequestEntity requestEntity = new ClovaSpeechClient.NestRequestEntity();

        // filePath를 사용하여 파일 업로드
        final String result = clovaSpeechClient.upload(new File(filePath), requestEntity);
        return result;
    }
}


