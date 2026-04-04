package com.example.water.domain.chat.service;

import com.example.water.domain.chat.dto.RedisChatMessageDto;
import com.example.water.domain.chat.entity.ChatMessage;
import com.example.water.domain.chat.entity.ChatSession;
import com.example.water.domain.chat.repository.ChatMessageRepository;
import com.example.water.domain.chat.repository.ChatSessionRepository;
import com.example.water.global.common.SessionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatPersistenceService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRedisService chatRedisService;

    @Transactional
    public void completeSession(Long sessionId) {
        ChatSession chatSession = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 세션이 존재하지 않습니다."));

        List<RedisChatMessageDto> redisMessages = chatRedisService.getMessages(sessionId);

        if (!redisMessages.isEmpty()) {
            List<ChatMessage> chatMessages = redisMessages.stream()
                    .map(dto -> {
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setSession(chatSession);
                        chatMessage.setSenderType(dto.getSenderType());
                        chatMessage.setSequence(dto.getSequence());
                        chatMessage.setEmotion(dto.getEmotion());
                        chatMessage.setSingleChip(dto.getSingleChip());
                        chatMessage.setContent(dto.getContent());
                        return chatMessage;
                    })
                    .toList();

            chatMessageRepository.saveAll(chatMessages);
        }

        chatSession.setStatus(SessionStatus.COMPLETED);
        chatRedisService.clear(sessionId);
    }

    @Transactional
    public void interruptSession(Long sessionId) {
        ChatSession chatSession = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 세션이 존재하지 않습니다."));

        List<RedisChatMessageDto> redisMessages = chatRedisService.getMessages(sessionId);

        if (!redisMessages.isEmpty()) {
            List<ChatMessage> chatMessages = redisMessages.stream()
                    .map(dto -> {
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setSession(chatSession);
                        chatMessage.setSenderType(dto.getSenderType());
                        chatMessage.setSequence(dto.getSequence());
                        chatMessage.setEmotion(dto.getEmotion());
                        chatMessage.setSingleChip(dto.getSingleChip());
                        chatMessage.setContent(dto.getContent());
                        return chatMessage;
                    })
                    .toList();

            chatMessageRepository.saveAll(chatMessages);
        }

        chatSession.setStatus(SessionStatus.INTERRUPTED);
        chatRedisService.clear(sessionId);
    }
}