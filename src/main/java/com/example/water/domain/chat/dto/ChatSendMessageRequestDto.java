package com.example.water.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatSendMessageRequestDto {

    @NotBlank
    private String content;

    private String emotion;
    private String singleChip;
}