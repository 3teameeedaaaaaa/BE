package com.example.water.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.io.IOException;
import java.util.Map;

@Service
public class AiCommunicationService {

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public String getAiAnalysis(Map<String, Object> requestParams) {
        // 1. 리다이렉트 방지 설정 (POST가 GET으로 변하는 것 차단)
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
            }
        };

        factory.setConnectTimeout(5000);
        factory.setReadTimeout(30000);
        RestTemplate restTemplate = new RestTemplate(factory);

        // 2. 주소 조립
        String baseUrl = aiServerUrl.trim();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        String url = baseUrl + "/api/chat";

        try {
            // 3. [피드백 반영] JS의 allowedHeaders를 자바 헤더로 변환
            HttpHeaders headers = new HttpHeaders();

            // 'Content-Type': application/json
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 'ngrok-skip-browser-warning': ngrok 통과용
            headers.set("ngrok-skip-browser-warning", "true"    );

            // 'Authorization': 혹시 나중에 토큰이 필요할 경우를 대비 (현재는 빈 값 가능)
            // headers.set("Authorization", "Bearer your_token_here");

            // 4. 데이터와 헤더를 하나의 요청 객체(Entity)로 묶음
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestParams, headers);

            System.out.println(">>> [최종 발사] URL: " + url);
            System.out.println(">>> [보내는 헤더]: " + headers);

            // 5. POST 요청 실행
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return response.getBody();

        } catch (Exception e) {
            System.err.println("!!! [통신 에러]: " + e.getMessage());
            return "{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}";
        }
    }
}