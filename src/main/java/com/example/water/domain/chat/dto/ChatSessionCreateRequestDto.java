package com.example.water.domain.chat.dto;

import com.example.water.global.common.SessionMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatSessionCreateRequestDto {

    @NotNull
    private Long stockId;

    @NotBlank
    private String customStockName;

    @NotNull
    private SessionMode sessionMode;

    @NotBlank
    private String emotion;

    @NotBlank
    private String content;
}