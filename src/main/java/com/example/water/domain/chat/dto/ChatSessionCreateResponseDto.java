package com.example.water.domain.chat.dto;

import com.example.water.global.common.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatSessionCreateResponseDto {
    private Long sessionId;
    private SessionStatus status;
}