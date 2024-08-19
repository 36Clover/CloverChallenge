package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MicAuthController {

    @GetMapping("/mainpage")
    public String mainPage(){
        return "mainpage"; // 템플릿 반환
    }

    @GetMapping("/rec")
    public String micAuth(){
        return "micauth";
    }

    @PostMapping("/upload")
    public String uploadAudio(@RequestParam("audio") MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            System.out.println("파일이 비어 있습니다.");
            return "nexterr"; // 파일이 비어있는 경우 처리
        }

        String originalFilename = audioFile.getOriginalFilename();
        if (originalFilename == null) {
            System.out.println("파일 이름이 null입니다.");
            return "nexterr"; // 파일 이름이 null인 경우 처리
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension; // 고유한 파일 이름 생성
        String filePath = "C:\\Users\\hyoju\\Desktop\\testpath\\" + uniqueFilename; // 파일 저장 경로

        // 디렉토리 생성 (폴더가 존재하지 않으면 생성)
        File directory = new File("C:\\Users\\hyoju\\Desktop\\testpath\\");
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리 생성
        }

        try {
            audioFile.transferTo(new File(filePath));
            System.out.println("파일이 성공적으로 저장되었습니다: " + filePath);
            return "next";
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일 저장 중 오류 발생");
            return "nexterr";
        }
    }



}
