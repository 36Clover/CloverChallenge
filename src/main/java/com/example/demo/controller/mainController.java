package com.example.demo.controller;

import com.example.demo.Appli;
import com.example.demo.ClovaSpeechClient;
import com.example.demo.gpt_method.ConvertMp;
import com.example.demo.gpt_method.ConvertPdf;
import com.example.demo.gpt_method.ConvertPointText;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Controller
public class mainController {

    /* 메인페이지 */
    @GetMapping("/main")
    public String main(Model model){
        return "main";
    }

    /* 메인페이지 */
    @GetMapping("/voice")
    public String voice(Model model){
        return "voice";
    }
    @GetMapping("/file")
    public String file(Model model){
        return "file";
    }
    @GetMapping("/file2")
    public String file2(Model model){
        return "file2";
    }
    @GetMapping("/QType")
    public String qType(Model model){
        return "qType";
    }
 /*   @GetMapping("/result")
    public String showResult(Model model) {
        // 콘솔에서 받은 결과 값(이 부분은 실제로 Java에서 데이터 생성/처리 과정에 해당)
        String summarizedText = new ConvertPdf().c_pdf();
        String brailleText = new ConvertPointText().p_text();

        // 모델에 데이터를 추가
        model.addAttribute("summarizedText", summarizedText);
        model.addAttribute("brailleText", brailleText);

        // Thymeleaf 템플릿 파일 이름 반환 (result.html)
        return "result";
    }*/

    @PostMapping("/result")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        if (!file.isEmpty()) {
            try {
                // 파일을 임시 디렉토리에 저장
                String filePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
                File dest = new File(filePath);
                file.transferTo(dest);

                // ConvertPdf 클래스의 c_pdf 메서드에 파일 경로 전달
                ConvertPdf converter = new ConvertPdf();
                String summarizedText = converter.c_pdf(filePath);
                String brailleText = new ConvertPointText().p_text(summarizedText);

                // 모델에 데이터를 추가
                model.addAttribute("summarizedText", summarizedText);
                model.addAttribute("brailleText", brailleText);


                // 요약 결과를 모델에 추가하여 Thymeleaf에서 접근 가능하도록 함
                model.addAttribute("summary", summarizedText);
                model.addAttribute("brailleText", brailleText);
                return "result"; // 결과를 표시할 페이지로 이동

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "error"; // 오류 시 이동할 페이지
    }

    @PostMapping("/result2")
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
                ConvertMp converter2 = new ConvertMp();
                String sum2= converter2.c_mp(sum);
                String brailleText = new ConvertPointText().p_text(sum);

                // 모델에 데이터를 추가
                model.addAttribute("summarizedText", sum2);
                model.addAttribute("brailleText", brailleText);


                // 요약 결과를 모델에 추가하여 Thymeleaf에서 접근 가능하도록 함
                model.addAttribute("summary",sum2);
                model.addAttribute("brailleText", brailleText);
                return "result"; // 결과를 표시할 페이지로 이동

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "error"; // 오류 시 이동할 페이지
    }

    @GetMapping("/quize")
    public String quize(Model model){
        return "quize";
    }
}
