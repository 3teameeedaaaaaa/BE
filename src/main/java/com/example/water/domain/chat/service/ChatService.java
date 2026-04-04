package com.example.water.domain.chat.service;

import com.example.water.domain.chat.dto.ChatSendMessageRequestDto;
import com.example.water.domain.chat.dto.RedisChatMessageDto;
import com.example.water.domain.chat.entity.ChatSession;
import com.example.water.domain.chat.entity.SenderType;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatSessionService chatSessionService;
    private final ChatRedisService chatRedisService;
    private final ChatSseService chatSseService;
    private final ChatAiService chatAiService;

    public void sendMessage(Long sessionId, ChatSendMessageRequestDto request, HttpSession httpSession) {
        chatSessionService.validateOwner(sessionId, httpSession);

        ChatSession chatSession = chatSessionService.getSession(sessionId);

        int userSequence = chatRedisService.nextSequence(sessionId);

        RedisChatMessageDto userMessage = RedisChatMessageDto.builder()
                .sequence(userSequence)
                .senderType(SenderType.USER)
                .emotion(request.getEmotion())
                .singleChip(request.getSingleChip())
                .commonChip(request.getCommonChip())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        chatRedisService.appendMessage(sessionId, userMessage);
        chatSseService.send(sessionId, "chat", userMessage);

        String aiReply = chatAiService.generateReply(chatSession, request.getContent());

        int aiSequence = chatRedisService.nextSequence(sessionId);

        RedisChatMessageDto aiMessage = RedisChatMessageDto.builder()
                .sequence(aiSequence)
                .senderType(SenderType.AI)
                .content(aiReply)
                .createdAt(LocalDateTime.now())
                .build();

        chatRedisService.appendMessage(sessionId, aiMessage);
        chatSseService.send(sessionId, "chat", aiMessage);
    }
}