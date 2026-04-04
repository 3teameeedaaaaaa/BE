package com.example.water.domain.analysis.dto;

import com.example.water.global.common.SessionMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AnalysisAiRequestDto {
    private Long sessionId;
    private SessionMode sessionMode;
    private String stockName;
    private List<AnalysisMessageDto> messages;
}