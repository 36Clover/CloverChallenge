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

    @GetMapping("/")
    public String micAuth(){
        return "micauth";
    }

    @PostMapping("/upload")
    public String uploadAudio(@RequestParam("audio") MultipartFile audioFile) {
        String originalFilename = audioFile.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension; // 고유한 파일 이름 생성
        String filePath = "C:\\Users\\hyoju\\Desktop\\testpath\\" + uniqueFilename; // 파일 저장 경로

        try {
            audioFile.transferTo(new File(filePath));
            System.out.println("1 여기");
            return "next";
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("2 여기");
            return "nexterr";
        }
    }


}
