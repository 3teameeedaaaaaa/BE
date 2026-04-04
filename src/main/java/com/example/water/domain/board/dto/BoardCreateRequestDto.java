package com.example.water.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCreateRequestDto {

    @NotNull
    private Long resultId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}