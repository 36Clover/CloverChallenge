package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    @GetMapping("/QType")
    public String qType(Model model){
        return "qType";
    }
    @GetMapping("/result")
    public String result(Model model){
        return "result";
    }

    @GetMapping("/quize")
    public String quize(Model model){
        return "quize";
    }
}
