package com.example.water.domain.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AnalysisAiResponseDto {
    private String distortionTag;
    private String reflectionSummary;
    private Integer togetherAgreePercent;
    private Integer togetherOtherPercent;
    private String togetherComment;
}