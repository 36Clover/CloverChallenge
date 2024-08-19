package com.example.demo.controller;

import com.example.demo.ClovaSpeechClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class ChooseController {

    String result = "";

    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    public ResponseEntity<List<Map<String, String>>> uploadAudio(@RequestParam("audio") MultipartFile file) {
        String result;

        String uploadDir = "C:\\Users\\hyoju\\Desktop\\testpath\\";
        String filePath = uploadDir + file.getOriginalFilename(); // 파일 경로 구성
        // 파일 저장 및 처리 로직

        result = pa_json(filePath);
//        try {
//
//
//            // 파일을 지정한 경로에 저장
//            file.transferTo(new File(filePath)); // 파일 저장
//
//            // pa_json 메서드 호출
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(Collections.singletonList(Map.of("error", "파일 저장 중 오류 발생: " + e.getMessage())));
//        }

        // JSON 파싱 및 화자별 인식 결과 추출
        Map<String, Object> resultMap = new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {}.getType());
        List<Map<String, Object>> segments = (List<Map<String, Object>>) resultMap.get("segments");
        List<Map<String, String>> speakerSegments = new ArrayList<>();


        if (segments != null) {
            for (Map<String, Object> segment : segments) {
                Map<String, String> speaker = (Map<String, String>) segment.get("speaker");
                String speakerLabel = speaker.get("label");
                String text = (String) segment.get("text");
                speakerSegments.add(Map.of("speaker", "", "text", text));
            }
        }

        return ResponseEntity.ok(speakerSegments); // 화자별 인식 결과를 JSON 형태로 반환
    }

    public String pa_json(String filePath) {
        final ClovaSpeechClient clovaSpeechClient = new ClovaSpeechClient();
        ClovaSpeechClient.NestRequestEntity requestEntity = new ClovaSpeechClient.NestRequestEntity();

        // filePath를 사용하여 파일 업로드
        final String result = clovaSpeechClient.upload(new File(filePath), requestEntity);
        return result;
    }
}
