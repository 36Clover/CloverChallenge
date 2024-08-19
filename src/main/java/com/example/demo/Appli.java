package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static java.awt.SystemColor.text;

public class Appli {

    public String mk2(String pas) {
        try {
            String response = pas;
            Map<String, Object> result = new Gson().fromJson(response, new TypeToken<Map<String, Object>>() {
            }.getType());

            // 화자별 인식 결과 segment 추출
            List<Map<String, Object>> segments = (List<Map<String, Object>>) result.get("segments");
            List<Map<String, String>> speakerSegments = new ArrayList<>();

            if (segments != null) {
                for (Map<String, Object> segment : segments) {
                    Map<String, String> speaker = (Map<String, String>) segment.get("speaker");
                    String speakerLabel = speaker.get("label");
                    String text = (String) segment.get("text");
                    speakerSegments.add(Map.of("speaker", speakerLabel, "text", text));
                }
            }

            // 화자별 인식 결과 segment 출력
            String accumulatedText = "";  // text 누적값을 저장할 변수 선언

            for (Map<String, String> speakerSegment : speakerSegments) {
                String speakerLabel = speakerSegment.get("speaker");
                String text = speakerSegment.get("text");
                System.out.println(text);
                accumulatedText += text + text;  // text를 누적
            }

            return accumulatedText;  // 최종 누적된 값을 반환

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

}

