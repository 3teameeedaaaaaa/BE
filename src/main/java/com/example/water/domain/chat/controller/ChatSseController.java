package com.example.water.domain.chat.controller;

import com.example.water.domain.chat.service.ChatSessionService;
import com.example.water.domain.chat.service.ChatSseService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatSseController {

    private final ChatSseService chatSseService;
    private final ChatSessionService chatSessionService;

    @GetMapping(value = "/sessions/{sessionId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(
            @PathVariable Long sessionId,
            HttpSession session
    ) {
        chatSessionService.validateOwner(sessionId, session);
        return chatSseService.connect(sessionId);
    }
}