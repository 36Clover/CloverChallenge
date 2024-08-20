package com.example.demo.controller;

import com.example.demo.Appli;
import com.example.demo.ClovaSpeechClient;
import com.example.demo.gpt_method.ConvertMp;
import com.example.demo.gpt_method.ConvertPdf;
import com.example.demo.gpt_method.ConvertPointText;
import com.example.demo.gpt_method.MakeQuestion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class QuestionController {
    private final MakeQuestion makeQuestionService = new MakeQuestion();

@PostMapping("/quiz")
    public String uploadFile2(@RequestParam("file") MultipartFile file, Model model) {
        if (!file.isEmpty()) {
            try {

                // 파일을 임시 디렉토리에 저장
                String filePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
                File dest = new File(filePath);
                file.transferTo(dest);

                // ConvertPdf 클래스의 c_pdf 메서드에 파일 경로 전달
                ClovaSpeechClient converter = new ClovaSpeechClient();
                String summarizedText = converter.pa_json(filePath);
                String sum = new Appli().mk2(summarizedText);

                String data= sum;

                List<Map<String, String>> questions = makeQuestionService.make_question(data);

                System.out.println(questions);

                // 모델에 데이터 추가
                model.addAttribute("questions", questions);

                // Thymeleaf 템플릿 이름 반환
                return "quize";


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "error"; // 오류 시 이동할 페이지
    }

    @PostMapping("/quize")
    public String uploadFile3(@RequestParam("file") MultipartFile file, Model model) {
        if (!file.isEmpty()) {
            try {

                // 파일을 임시 디렉토리에 저장
                String filePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
                File dest = new File(filePath);
                file.transferTo(dest);

                ConvertPdf converter = new ConvertPdf();
                String summarizedText = converter.c_pdf(filePath);

                // ConvertPdf 클래스의 c_pdf 메서드에 파일 경로 전달
         /*       String sum = new Appli().mk2(summarizedText);*/

                String data= summarizedText;

                List<Map<String, String>> questions = makeQuestionService.make_question(data);

                System.out.println(questions);

                // 모델에 데이터 추가
                model.addAttribute("questions", questions);

                // Thymeleaf 템플릿 이름 반환
                return "quize";


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "error"; // 오류 시 이동할 페이지
    }
}
