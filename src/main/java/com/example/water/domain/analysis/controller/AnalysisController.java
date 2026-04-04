package com.example.water.domain.analysis.controller;

import com.example.water.domain.analysis.dto.AnalysisAiRequestDto;
import com.example.water.domain.analysis.dto.AnalysisAiResponseDto;
import com.example.water.domain.analysis.dto.AnalysisResultResponseDto;
import com.example.water.domain.analysis.dto.UserDecisionRequestDto;
import com.example.water.domain.analysis.service.AnalysisAiService;
import com.example.water.domain.analysis.service.AnalysisService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis/sessions")
public class AnalysisController {

    private final AnalysisService analysisService;
    private final AnalysisAiService aiService;

    @PostMapping("/{sessionId}")
    public ResponseEntity<AnalysisResultResponseDto> createAnalysisResult(
            @PathVariable Long sessionId,
            HttpSession session
    ) {
        return ResponseEntity.ok(analysisService.createAnalysisResult(sessionId, session));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<AnalysisResultResponseDto> getAnalysisResult(
            @PathVariable Long sessionId,
            HttpSession session
    ) {
        return ResponseEntity.ok(analysisService.getAnalysisResult(sessionId, session));
    }

    @PatchMapping("/{sessionId}/decision")
    public ResponseEntity<Void> updateUserDecision(
            @PathVariable Long sessionId,
            @Valid @RequestBody UserDecisionRequestDto request,
            HttpSession session
    ) {
        analysisService.updateUserDecision(sessionId, request.getUserDecision(), session);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/ask")
    public ResponseEntity<AnalysisAiResponseDto> askAi(@RequestBody AnalysisAiRequestDto requestDto) {

        // 1. 규태님(AI) 서버 규격에 맞게 데이터를 재조립합니다.
        Map<String, Object> aiParams = new HashMap<>();

        // 기본 정보 매핑
        aiParams.put("mode", requestDto.getMode());
        aiParams.put("ticker", requestDto.getTicker());
        aiParams.put("emotion", requestDto.getEmotion());

        // [보완] situation과 singleChip을 모두 text 필드에 합치기 (규태님 서버 에러 방지)
        String sit = (requestDto.getSituation() != null) ? requestDto.getSituation() : "";
        String chip = (requestDto.getSingleChip() != null) ? requestDto.getSingleChip() : "";
        String userText = (requestDto.getText() != null) ? requestDto.getText() : "";

        // 형식 예: "[급등 중 / 뉴스를 봤어요] 지금 사면 물릴까요?"
        String combinedText = String.format("[%s / %s] %s", sit, chip, userText).trim();

        aiParams.put("text", combinedText);
        aiParams.put("turn_number", requestDto.getTurn_number() != null ? requestDto.getTurn_number() : 1);

        // [보완] 이전 왜곡 유형 및 히스토리 Null 방어 (첫 턴 테스트 시 에러 방지)
        aiParams.put("previous_distortion_type",
                (requestDto.getPrevious_distortion_type() != null) ? requestDto.getPrevious_distortion_type() : "");

        aiParams.put("conversation_history",
                (requestDto.getConversation_history() != null) ? requestDto.getConversation_history() : new ArrayList<>());

        // 2. 가공된 데이터를 서비스로 전달하고 결과 수신
        // [보완] 리턴 타입을 AiResponseDto로 받아 영섭님이 각 필드를 쉽게 쓰게 함
        AnalysisAiResponseDto response = aiService.getAiAnalysis(aiParams);

        // [보완] 1턴 스킵 금지 로직: turn_number가 1이면 무조건 필수 답변으로 설정
        if (requestDto.getTurn_number() != null && requestDto.getTurn_number() == 1) {
            if (response != null) {
                response.setRequired(true);
            }
        }

        // ResponseEntity로 감싸서 200 OK와 함께 반환
        return ResponseEntity.ok(response);
    }
}