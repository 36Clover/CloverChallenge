package com.example.demo.controller;

import com.example.demo.ClovaSpeechClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class ChooseController {

    String result = "";

    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadAudio(@RequestParam("audio") MultipartFile file) {
        // 파일 저장 경로
        String uploadDir = "C:\\Users\\hyoju\\Desktop\\testpath\\";
        String filePath = uploadDir + file.getOriginalFilename(); // 파일 경로 구성

        // 파일을 지정한 경로에 저장
//        try {
//            file.transferTo(new File(filePath)); // 파일 저장
//        } catch (IOException e) {
//            e.printStackTrace(); // 예외 스택 트레이스를 로그에 남김
//            return ResponseEntity.status(500).body("파일 저장 중 오류 발생: " + e.getMessage()); // 오류 메시지 반환
//        }

        // pa_json 메서드 호출
        result = pa_json(filePath);
        return ResponseEntity.ok(result); // JSON 응답 반환
    }

    public String pa_json(String filePath) {
        final ClovaSpeechClient clovaSpeechClient = new ClovaSpeechClient();
        ClovaSpeechClient.NestRequestEntity requestEntity = new ClovaSpeechClient.NestRequestEntity();

        // filePath를 사용하여 파일 업로드
        final String result = clovaSpeechClient.upload(new File(filePath), requestEntity);
        return result;
    }

    public String pa_json2(){

        return result;
    }

}
