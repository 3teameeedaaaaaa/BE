package com.example.water.domain.board.dto;

import com.example.water.domain.board.dto.CalendarDayCountDto;
import com.example.water.domain.board.dto.DistortionStatDto;
import com.example.water.domain.board.dto.RecentRecordDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CalendarSummaryResponseDto {
    private List<CalendarDayCountDto> dayCounts;
    private List<DistortionStatDto> distortionStats;
    private List<RecentRecordDto> recentRecords;
}