package com.example.water.controller;

import com.example.water.dto.AiRequestDto;
import com.example.water.service.AiCommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AiController {

    private final AiCommunicationService aiService;

    /**
     * 영섭님(FE) 호출 주소: POST http://3.36.62.164:8080/api/chat/ask
     */
    @PostMapping("/ask")
    public String askAi(@RequestBody AiRequestDto requestDto) {

        // 1. 규태님(AI) 서버 규격에 맞게 데이터를 재조립합니다.
        Map<String, Object> aiParams = new HashMap<>();

        aiParams.put("mode", requestDto.getMode());
        aiParams.put("ticker", requestDto.getTicker());
        aiParams.put("emotion", requestDto.getEmotion());

        // [핵심] 칩 데이터(situation)와 직접 입력(text)을 하나로 합칩니다.
        // 예: "[급등 중이에요] 지금 팔아야 할까요?"
        String combinedText = String.format("[%s] %s",
                requestDto.getSituation(),
                requestDto.getText() != null ? requestDto.getText() : "").trim();

        aiParams.put("text", combinedText);
        aiParams.put("turn_number", requestDto.getTurn_number());

        // null 방지 처리
        aiParams.put("previous_distortion_type",
                requestDto.getPrevious_distortion_type() != null ? requestDto.getPrevious_distortion_type() : "");

        aiParams.put("conversation_history", requestDto.getConversation_history());

        // 2. 가공된 데이터를 들고 서비스(ngrok 통로)로 발사!
        return aiService.getAiAnalysis(aiParams);
    }
}