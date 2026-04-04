package com.example.water.domain.analysis.dto;

import com.example.water.domain.chat.entity.SenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AnalysisMessageDto {
    private Integer sequence;
    private SenderType senderType;
    private String content;
}