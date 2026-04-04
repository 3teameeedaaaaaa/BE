package com.example.water.domain.analysis.service;

import com.example.water.domain.analysis.dto.AnalysisAiResponseDto;
import com.example.water.domain.analysis.dto.AnalysisAiResponseDto; // [추가] 응답 DTO 임포트
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.io.IOException;
import java.util.Map;

@Service
public class AnalysisAiService {

    @Value("${ai.server.url}")
    private String aiServerUrl;

    // [변경] 리턴 타입을 String에서 AiResponseDto로 변경
    public AnalysisAiResponseDto getAiAnalysis(Map<String, Object> requestParams) {

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

        // 주소 조립
        String baseUrl = aiServerUrl.trim();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        String url = baseUrl + "/api/chat";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("ngrok-skip-browser-warning", "true");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestParams, headers);

            System.out.println(">>> [최종 발사] URL: " + url);

            // [변경] String.class 대신 AiResponseDto.class를 사용하여 자동으로 객체 변환
            ResponseEntity<AnalysisAiResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    AnalysisAiResponseDto.class
            );

            return response.getBody();

        } catch (Exception e) {
            System.err.println("!!! [통신 에러]: " + e.getMessage());
            // 에러 발생 시 빈 객체를 반환하여 컨트롤러가 터지지 않게 방어
            return new AnalysisAiResponseDto();
        }
    }
}