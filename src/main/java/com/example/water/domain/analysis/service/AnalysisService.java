package com.example.water.domain.analysis.service;

import com.example.water.domain.analysis.dto.*;
import com.example.water.domain.analysis.entity.AnalysisResult;
import com.example.water.domain.analysis.entity.CognitiveDistortion;
import com.example.water.domain.analysis.repository.AnalysisResultRepository;
import com.example.water.domain.analysis.repository.CognitiveDistortionRepository;
import com.example.water.domain.chat.entity.ChatMessage;
import com.example.water.domain.chat.entity.ChatSession;
import com.example.water.domain.chat.repository.ChatMessageRepository;
import com.example.water.domain.chat.repository.ChatSessionRepository;
import com.example.water.domain.member.dto.SessionMemberDto;
import com.example.water.global.common.SessionMode;
import com.example.water.global.common.UserDecision;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AnalysisService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CognitiveDistortionRepository cognitiveDistortionRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final AnalysisAiService analysisAiService;

    public AnalysisResultResponseDto createAnalysisResult(Long sessionId, HttpSession httpSession) {
        ChatSession session = getOwnedSession(sessionId, httpSession);

        analysisResultRepository.findBySessionId(sessionId)
                .ifPresent(result -> {
                    throw new IllegalArgumentException("이미 분석 결과가 생성되었습니다.");
                });

        List<ChatMessage> messages = chatMessageRepository.findBySessionIdOrderBySequenceAsc(sessionId);

        Map<String, Object> aiParams = new HashMap<>();
        aiParams.put("mode", session.getSessionMode().toString().toLowerCase());
        aiParams.put("ticker", session.getStock() != null ? session.getStock().getName() : session.getCustomStockName());
        aiParams.put("emotion", "");
        aiParams.put("text", messages.isEmpty() ? "" : messages.get(messages.size() - 1).getContent());
        aiParams.put("turn_number", 2);

        List<Map<String, String>> history = messages.stream()
                .map(m -> {
                    Map<String, String> msg = new HashMap<>();
                    msg.put("role", m.getSenderType().toString().equals("USER") ? "user" : "assistant");
                    msg.put("content", m.getContent());
                    return msg;
                }).toList();
        aiParams.put("conversation_history", history);

        AnalysisAiResponseDto aiResponse = analysisAiService.getAiAnalysis(aiParams);

        // [수정 포인트 1] getDistortionTag() -> getDistortion_tag() (DTO 필드명과 일치)
        CognitiveDistortion distortion = cognitiveDistortionRepository.findByTag(aiResponse.getDistortion_tag())
                .orElseThrow(() -> new IllegalArgumentException("인지왜곡 마스터 데이터가 없습니다. tag=" + aiResponse.getDistortion_tag()));

        AnalysisResult result = new AnalysisResult();
        result.setSession(session);
        result.setDistortion(distortion);

        // [수정 포인트 2] getReflectionSummary() -> getReflection_summary()
        result.setReflectionSummary(aiResponse.getReflection_summary());

        // [수정 포인트 3] DTO에 없는 퍼센트 필드 제거 및 코멘트 기본값 처리
        // aiResponse에 together_comment가 있다면 getTogether_comment()로 호출
        result.setTogetherComment("분석이 완료되었습니다.");

        analysisResultRepository.save(result);

        return toResponse(result, session.getSessionMode());
    }

    @Transactional(readOnly = true)
    public AnalysisResultResponseDto getAnalysisResult(Long sessionId, HttpSession httpSession) {
        ChatSession session = getOwnedSession(sessionId, httpSession);

        AnalysisResult result = analysisResultRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("분석 결과가 없습니다."));

        return toResponse(result, session.getSessionMode());
    }

    public void updateUserDecision(Long sessionId, UserDecision userDecision, HttpSession httpSession) {
        ChatSession session = getOwnedSession(sessionId, httpSession);

        if (session.getSessionMode() != SessionMode.PRE) {
            throw new IllegalArgumentException("사전 세션에서만 사용자 선택을 저장할 수 있습니다.");
        }

        AnalysisResult result = analysisResultRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("분석 결과가 없습니다."));

        result.setUserDecision(userDecision);
    }

    private AnalysisResultResponseDto toResponse(AnalysisResult result, SessionMode sessionMode) {
        return AnalysisResultResponseDto.builder()
                .sessionId(result.getSession().getId())
                .sessionMode(sessionMode)
                .distortionTag(result.getDistortion().getTag())
                .distortionTypeName(result.getDistortion().getTypeName())
                .reflectionSummary(result.getReflectionSummary())
                .togetherComment(result.getTogetherComment())
                .decisionRequired(sessionMode == SessionMode.PRE)
                .userDecision(result.getUserDecision())
                .build();
    }

    private ChatSession getOwnedSession(Long sessionId, HttpSession session) {
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

        if (loginMember == null) {
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        ChatSession chatSession = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 세션이 존재하지 않습니다."));

        if (!chatSession.getMember().getId().equals(loginMember.getId())) {
            throw new IllegalArgumentException("본인의 세션만 접근할 수 있습니다.");
        }

        return chatSession;
    }
}