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

import java.util.List;

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

        String stockName = session.getStock() != null
                ? session.getStock().getName()
                : session.getCustomStockName();

        AnalysisAiRequestDto aiRequest = AnalysisAiRequestDto.builder()
                .sessionId(session.getId())
                .sessionMode(session.getSessionMode())
                .stockName(stockName)
                .messages(messages.stream()
                        .map(m -> AnalysisMessageDto.builder()
                                .sequence(m.getSequence())
                                .senderType(m.getSenderType())
                                .content(m.getContent())
                                .build())
                        .toList())
                .build();

        AnalysisAiResponseDto aiResponse = analysisAiService.analyze(aiRequest);

        CognitiveDistortion distortion = cognitiveDistortionRepository.findByTag(aiResponse.getDistortionTag())
                .orElseThrow(() -> new IllegalArgumentException("인지왜곡 마스터 데이터가 없습니다. tag=" + aiResponse.getDistortionTag()));

        AnalysisResult result = new AnalysisResult();
        result.setSession(session);
        result.setDistortion(distortion);
        result.setReflectionSummary(aiResponse.getReflectionSummary());
        result.setTogetherAgreePercent(aiResponse.getTogetherAgreePercent());
        result.setTogetherOtherPercent(aiResponse.getTogetherOtherPercent());
        result.setTogetherComment(aiResponse.getTogetherComment());

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
                .togetherAgreePercent(result.getTogetherAgreePercent())
                .togetherOtherPercent(result.getTogetherOtherPercent())
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