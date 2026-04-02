package com.example.water.domain.chat.controller;

import com.example.water.domain.chat.dto.ChatSessionCreateRequestDto;
import com.example.water.domain.chat.service.ChatSessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat/sessions")
@RequiredArgsConstructor
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    @PostMapping
    public Long createChatSession(@Valid @RequestBody ChatSessionCreateRequestDto request,
                                  HttpSession session) {
        return chatSessionService.createChatSession(request, session);
    }
}