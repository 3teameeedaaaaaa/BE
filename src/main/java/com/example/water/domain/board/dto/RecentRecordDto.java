package com.example.water.domain.board.dto;

import com.example.water.global.common.SessionMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RecentRecordDto {
    private Long sessionId;
    private String stockName;
    private String emotionLabel;
    private String distortionTypeName;
    private SessionMode sessionMode;
    private String reflectionSummary;
    private String createdDate;
}