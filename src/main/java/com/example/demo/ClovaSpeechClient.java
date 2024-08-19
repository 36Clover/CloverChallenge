package com.example.demo;

import com.google.gson.Gson;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClovaSpeechClient {

    // Clova Speech secret key
    private static final String SECRET = "38346a50f86f4c6bb3abeb3a4a23c82c";
    // Clova Speech invoke URL
    private static final String INVOKE_URL = "https://clovaspeech-gw.ncloud.com/external/v1/8854/86059d4ed4a8f3eee0da15b280dbc72b1b04a5733a61d93482643d56090c26f7";

    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private Gson gson = new Gson();

    private static final Header[] HEADERS = new Header[] {
            new BasicHeader("Accept", "application/json"),
            new BasicHeader("X-CLOVASPEECH-API-KEY", SECRET),
    };

    public static class Boosting {
        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }

    public static class Diarization {
        private Boolean enable = Boolean.FALSE;
        private Integer speakerCountMin;
        private Integer speakerCountMax;

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public Integer getSpeakerCountMin() {
            return speakerCountMin;
        }

        public void setSpeakerCountMin(Integer speakerCountMin) {
            this.speakerCountMin = speakerCountMin;
        }

        public Integer getSpeakerCountMax() {
            return speakerCountMax;
        }

        public void setSpeakerCountMax(Integer speakerCountMax) {
            this.speakerCountMax = speakerCountMax;
        }
    }

    public static class Sed {
        private Boolean enable = Boolean.FALSE;

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }
    }

    public static class NestRequestEntity {
        private String language = "ko-KR";
        //completion optional, sync/async
        private String completion = "sync";
        //optional, used to receive the analyzed results
        private String callback;
        //optional, any data
        private Map<String, Object> userdata;
        private Boolean wordAlignment = Boolean.TRUE;
        private Boolean fullText = Boolean.TRUE;
        //boosting object array
        private List<Boosting> boostings;
        //comma separated words
        private String forbiddens;
        private Diarization diarization;
        private Sed sed;

        public Sed getSed() {
            return sed;
        }

        public void setSed(Sed sed) {
            this.sed = sed;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getCompletion() {
            return completion;
        }

        public void setCompletion(String completion) {
            this.completion = completion;
        }

        public String getCallback() {
            return callback;
        }

        public Boolean getWordAlignment() {
            return wordAlignment;
        }

        public void setWordAlignment(Boolean wordAlignment) {
            this.wordAlignment = wordAlignment;
        }

        public Boolean getFullText() {
            return fullText;
        }

        public void setFullText(Boolean fullText) {
            this.fullText = fullText;
        }

        public void setCallback(String callback) {
            this.callback = callback;
        }

        public Map<String, Object> getUserdata() {
            return userdata;
        }

        public void setUserdata(Map<String, Object> userdata) {
            this.userdata = userdata;
        }

        public String getForbiddens() {
            return forbiddens;
        }

        public void setForbiddens(String forbiddens) {
            this.forbiddens = forbiddens;
        }

        public List<Boosting> getBoostings() {
            return boostings;
        }

        public void setBoostings(List<Boosting> boostings) {
            this.boostings = boostings;
        }

        public Diarization getDiarization() {
            return diarization;
        }

        public void setDiarization(Diarization diarization) {
            this.diarization = diarization;
        }
    }

    /**
     * recognize media using URL
     * @param url required, the media URL
     * @param nestRequestEntity optional
     * @return string
     */
    public String url(String url, NestRequestEntity nestRequestEntity) {
        HttpPost httpPost = new HttpPost(INVOKE_URL + "/recognizer/url");
        httpPost.setHeaders(HEADERS);
        Map<String, Object> body = new HashMap<>();
        body.put("url", url);
        body.put("language", nestRequestEntity.getLanguage());
        body.put("completion", nestRequestEntity.getCompletion());
        body.put("callback", nestRequestEntity.getCallback());
        body.put("userdata", nestRequestEntity.getCallback());
        body.put("wordAlignment", nestRequestEntity.getWordAlignment());
        body.put("fullText", nestRequestEntity.getFullText());
        body.put("forbiddens", nestRequestEntity.getForbiddens());
        body.put("boostings", nestRequestEntity.getBoostings());
        body.put("diarization", nestRequestEntity.getDiarization());
        body.put("sed", nestRequestEntity.getSed());
        HttpEntity httpEntity = new StringEntity(gson.toJson(body), ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        return execute(httpPost);
    }

    /**
     * recognize media using Object Storage
     * @param dataKey required, the Object Storage key
     * @param nestRequestEntity optional
     * @return string
     */
    public String objectStorage(String dataKey, NestRequestEntity nestRequestEntity) {
        HttpPost httpPost = new HttpPost(INVOKE_URL + "/recognizer/object-storage");
        httpPost.setHeaders(HEADERS);
        Map<String, Object> body = new HashMap<>();
        body.put("dataKey", dataKey);
        body.put("language", nestRequestEntity.getLanguage());
        body.put("completion", nestRequestEntity.getCompletion());
        body.put("callback", nestRequestEntity.getCallback());
        body.put("userdata", nestRequestEntity.getCallback());
        body.put("wordAlignment", nestRequestEntity.getWordAlignment());
        body.put("fullText", nestRequestEntity.getFullText());
        body.put("forbiddens", nestRequestEntity.getForbiddens());
        body.put("boostings", nestRequestEntity.getBoostings());
        body.put("diarization", nestRequestEntity.getDiarization());
        body.put("sed", nestRequestEntity.getSed());
        StringEntity httpEntity = new StringEntity(gson.toJson(body), ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        return execute(httpPost);
    }

    /**
     *
     * recognize media using a file
     * @param file required, the media file
     * @param nestRequestEntity optional
     * @return string
     */
    public String upload(File file, NestRequestEntity nestRequestEntity) {
        HttpPost httpPost = new HttpPost(INVOKE_URL + "/recognizer/upload");
        httpPost.setHeaders(HEADERS);
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addTextBody("params", gson.toJson(nestRequestEntity), ContentType.APPLICATION_JSON)
                .addBinaryBody("media", file, ContentType.MULTIPART_FORM_DATA, file.getName())
                .build();
        httpPost.setEntity(httpEntity);
        return execute(httpPost);
    }

    private String execute(HttpPost httpPost) {
        try (final CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            final HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        final ClovaSpeechClient clovaSpeechClient = new ClovaSpeechClient();
        NestRequestEntity requestEntity = new NestRequestEntity();
        final String result =  clovaSpeechClient.upload(new File("C:\\Users\\mdk19\\Desktop\\test.mp3"), requestEntity);
        //final String result = clovaSpeechClient.url("file URL", requestEntity);
        //final String result = clovaSpeechClient.objectStorage("test.mp3", requestEntity);
        System.out.println(result);

    }
    public String pa_json() {
        final ClovaSpeechClient clovaSpeechClient = new ClovaSpeechClient();
        NestRequestEntity requestEntity = new NestRequestEntity();
        final String result =  clovaSpeechClient.upload(new File("C:\\Users\\mdk19\\Desktop\\test.mp3"), requestEntity);
        //final String result = clovaSpeechClient.url("file URL", requestEntity);
        //final String result = clovaSpeechClient.objectStorage("test.mp3", requestEntity);
        return result;
    }
}