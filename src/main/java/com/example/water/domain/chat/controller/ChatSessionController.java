package com.example.water.domain.chat.controller;

import com.example.water.domain.chat.dto.ChatSessionCreateRequestDto;
import com.example.water.domain.chat.dto.ChatSessionCreateResponseDto;
import com.example.water.domain.chat.service.ChatSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat", description = "채팅 세션 및 메시지 API")
@RestController
@RequestMapping("/api/chat/sessions")
@RequiredArgsConstructor
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    @Operation(summary = "채팅 세션 생성", description = "세션을 생성하고 첫 사용자 메시지는 Redis에 저장한다.")
    @PostMapping
    public ChatSessionCreateResponseDto createChatSession(
            @Valid @RequestBody ChatSessionCreateRequestDto request,
            HttpSession session
    ) {
        return chatSessionService.createChatSession(request, session);
    }
}