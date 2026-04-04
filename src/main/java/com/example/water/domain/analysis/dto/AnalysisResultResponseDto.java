package com.example.water.domain.analysis.dto;

import com.example.water.global.common.SessionMode;
import com.example.water.global.common.UserDecision;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AnalysisResultResponseDto {
    private Long sessionId;
    private SessionMode sessionMode;

    private String distortionTag;
    private String distortionTypeName;
    private String reflectionSummary;

    private Integer togetherAgreePercent;
    private Integer togetherOtherPercent;
    private String togetherComment;

    private boolean decisionRequired; // PRE면 true, POST면 false
    private UserDecision userDecision;
}