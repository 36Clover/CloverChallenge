/*
package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class App {

    public static void main(String[] args) {
        try {
            String response = new ClovaSpeechClient().pa_json();
            Map<String, Object> result = new Gson().fromJson(response, new TypeToken<Map<String, Object>>() {}.getType());

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
            for (Map<String, String> speakerSegment : speakerSegments) {
                String speakerLabel = speakerSegment.get("speaker");
                String text = speakerSegment.get("text");
                System.out.println(text);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String reqUpload(File file, String completion) throws ClientProtocolException, IOException {
        String url = "https://api-url-to-clova-speech"; // Clova Speech API URL 설정

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addPart("file", new FileBody(file));
        builder.addTextBody("completion", completion);

        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);

        HttpResponse response = httpClient.execute(uploadFile);
        HttpEntity responseEntity = response.getEntity();

        return EntityUtils.toString(responseEntity);
    }
}
*/
