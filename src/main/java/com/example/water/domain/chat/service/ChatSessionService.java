package com.example.water.domain.chat.service;

import com.example.water.domain.chat.dto.ChatSessionCreateRequestDto;
import com.example.water.domain.chat.dto.ChatSessionCreateResponseDto;
import com.example.water.domain.chat.dto.RedisChatMessageDto;
import com.example.water.domain.chat.entity.ChatSession;
import com.example.water.domain.chat.entity.SenderType;
import com.example.water.domain.chat.repository.ChatSessionRepository;
import com.example.water.domain.member.dto.SessionMemberDto;
import com.example.water.domain.member.entity.Member;
import com.example.water.domain.member.repository.MemberRepository;
import com.example.water.domain.stock.entity.Stock;
import com.example.water.domain.stock.repository.StockRepository;
import com.example.water.global.common.SessionStatus;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final MemberRepository memberRepository;
    private final StockRepository stockRepository;
    private final ChatRedisService chatRedisService;

    public ChatSessionCreateResponseDto createChatSession(ChatSessionCreateRequestDto request, HttpSession session) {
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

        if (loginMember == null) {
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        ChatSession chatSession = new ChatSession();
        chatSession.setMember(member);
        chatSession.setCustomStockName(request.getCustomStockName());
        chatSession.setSessionMode(request.getSessionMode());
        chatSession.setStatus(SessionStatus.ONGOING);

        if (request.getStockId() != null) {
            Stock stock = stockRepository.findById(request.getStockId())
                    .orElseThrow(() -> new IllegalArgumentException("종목이 존재하지 않습니다."));
            chatSession.setStock(stock);
        }

        ChatSession savedSession = chatSessionRepository.save(chatSession);

        int sequence = chatRedisService.nextSequence(savedSession.getId());

        RedisChatMessageDto firstUserMessage = RedisChatMessageDto.builder()
                .sequence(sequence)
                .senderType(SenderType.USER)
                .emotion(request.getEmotion())
                .singleChip(request.getSingleChip())
                .commonChip(request.getCommonChip())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        chatRedisService.appendMessage(savedSession.getId(), firstUserMessage);

        return new ChatSessionCreateResponseDto(savedSession.getId(), savedSession.getStatus());
    }

    @Transactional(readOnly = true)
    public ChatSession getSession(Long sessionId) {
        return chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 세션이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public void validateOwner(Long sessionId, HttpSession session) {
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

        if (loginMember == null) {
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        ChatSession chatSession = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 세션이 존재하지 않습니다."));

        if (!chatSession.getMember().getId().equals(loginMember.getId())) {
            throw new IllegalArgumentException("본인의 채팅 세션만 접근할 수 있습니다.");
        }
    }
}