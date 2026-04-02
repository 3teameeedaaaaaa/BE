package com.example.water.domain.chat.service;

import com.example.water.domain.chat.dto.ChatSessionCreateRequestDto;
import com.example.water.domain.chat.entity.ChatMessage;
import com.example.water.domain.chat.entity.ChatSession;
import com.example.water.domain.chat.entity.SenderType;
import com.example.water.domain.chat.repository.ChatMessageRepository;
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

@Service
@RequiredArgsConstructor
@Transactional
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final StockRepository stockRepository;

    public Long createChatSession(ChatSessionCreateRequestDto request, HttpSession session) {
        SessionMemberDto loginMember = (SessionMemberDto) session.getAttribute("loginMember");

        if (loginMember == null) {
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Stock stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new IllegalArgumentException("종목이 존재하지 않습니다."));

        ChatSession chatSession = new ChatSession();
        chatSession.setMember(member);
        chatSession.setStock(stock);
        chatSession.setCustomStockName(request.getCustomStockName());
        chatSession.setSessionMode(request.getSessionMode());
        chatSession.setStatus(SessionStatus.ONGOING);

        ChatSession savedSession = chatSessionRepository.save(chatSession);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSession(savedSession);
        chatMessage.setSenderType(SenderType.USER);
        chatMessage.setEmotion(request.getEmotion());
        chatMessage.setContent(request.getContent());
        chatMessage.setSkipped(false);

        chatMessageRepository.save(chatMessage);

        return savedSession.getId();
    }
}