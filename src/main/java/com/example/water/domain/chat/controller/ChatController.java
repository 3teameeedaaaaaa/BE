package com.example.water.domain.chat.controller;

import com.example.water.domain.chat.dto.ChatSendMessageRequestDto;
import com.example.water.domain.chat.dto.ChatSessionCreateRequestDto;
import com.example.water.domain.chat.dto.ChatSessionCreateResponseDto;
import com.example.water.domain.chat.service.ChatPersistenceService;
import com.example.water.domain.chat.service.ChatService;
import com.example.water.domain.chat.service.ChatSessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatSessionService chatSessionService;
    private final ChatService chatService;
    private final ChatPersistenceService chatPersistenceService;

    @PostMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<Void> sendMessage(
            @PathVariable Long sessionId,
            @Valid @RequestBody ChatSendMessageRequestDto request,
            HttpSession session
    ) {
        chatService.sendMessage(sessionId, request, session);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sessions/{sessionId}/complete")
    public ResponseEntity<Void> completeSession(
            @PathVariable Long sessionId,
            HttpSession session
    ) {
        chatSessionService.validateOwner(sessionId, session);
        chatPersistenceService.completeSession(sessionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sessions/{sessionId}/interrupt")
    public ResponseEntity<Void> interruptSession(
            @PathVariable Long sessionId,
            HttpSession session
    ) {
        chatSessionService.validateOwner(sessionId, session);
        chatPersistenceService.interruptSession(sessionId);
        return ResponseEntity.ok().build();
    }
}