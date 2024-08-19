package com.example.demo.controller;

import com.example.demo.gpt_method.MakeQuestion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final MakeQuestion makeQuestionService = new MakeQuestion();  // 서비스 인스턴스 생성

    String summary= "\n" +
            "네트워크의 7계층 모델은 OSI(Open Systems Interconnection) 모델을 말합니다. 이 모델은 컴퓨터 네트워킹의 구조를 설명하기 위해 설계된 7개의 계층으로 나뉩니다. 각 계층은 네트워크 통신의 특정 기능을 담당하며, 계층 간의 상호작용을 통해 데이터가 네트워크를 통해 전달됩니다. 각 계층은 다음과 같습니다:\n" +
            "\n" +
            "물리 계층 (Physical Layer): 전기적, 기계적, 절차적, 기능적 특성을 정의하여 데이터의 물리적 전송을 담당합니다. 예를 들어, 케이블, 스위치, 허브 등의 장비가 여기에 해당합니다.\n" +
            "\n" +
            "데이터 링크 계층 (Data Link Layer): 물리적 연결을 통해 데이터를 패킷으로 묶어 오류 검출 및 수정, 흐름 제어를 수행합니다. MAC 주소와 스위칭, 프레임 전송 등이 여기에 포함됩니다.\n" +
            "\n" +
            "네트워크 계층 (Network Layer): 데이터 패킷을 목적지까지 전달하는 경로를 설정하고 라우팅을 수행합니다. IP 주소와 라우터가 여기에 해당하며, 패킷 스위칭과 같은 기능을 담당합니다.";

    @GetMapping
    public String showQuestions(Model model) {
        // MakeQuestion 서비스 호출
        List<Map<String, String>> questions = makeQuestionService.make_question(summary);

        System.out.println(questions);

        // 모델에 데이터 추가
        model.addAttribute("questions", questions);

        // Thymeleaf 템플릿 이름 반환
        return "quize";
    }
}
