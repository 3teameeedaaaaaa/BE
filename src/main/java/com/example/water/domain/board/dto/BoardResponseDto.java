package com.example.water.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoardResponseDto {
    private Long boardId;
    private Long resultId;
    private String title;
    private String content;
    private int likeCount;

    private String distortionTypeName;
    private String reflectionSummary;
}