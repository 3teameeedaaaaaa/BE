package com.example.water.domain.chat.dto;

import com.example.water.global.common.SessionMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class ChatSessionCreateRequestDto {

    @Nullable
    private Long stockId;

    @Nullable
    private String customStockName;

    @NotNull
    private SessionMode sessionMode;

    @NotBlank
    private String emotion;

    @NotNull
    private String singleChip;

    @NotBlank
    private String content;
}