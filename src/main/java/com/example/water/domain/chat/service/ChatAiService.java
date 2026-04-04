package com.example.water.domain.chat.service;

import com.example.water.domain.chat.entity.ChatSession;
import org.springframework.stereotype.Service;

@Service
public class ChatAiService {

    public String generateReply(ChatSession session, String userInput) {
        return "AI 응답: \"" + userInput + "\"에 대해서 조금 더 객관적으로 생각해볼까요?";
    }
}