package com.example.water.domain.board.service;

import com.example.water.domain.analysis.entity.AnalysisResult;
import com.example.water.domain.analysis.repository.AnalysisResultRepository;
import com.example.water.domain.board.dto.CalendarDayCountDto;
import com.example.water.domain.board.dto.CalendarSummaryResponseDto;
import com.example.water.domain.board.dto.DistortionStatDto;
import com.example.water.domain.board.dto.RecentRecordDto;
import com.example.water.domain.member.dto.SessionMemberDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarAnalysisService {

    private final AnalysisResultRepository analysisResultRepository;

    public CalendarSummaryResponseDto getCalendarSummary(HttpSession httpSession) {
        SessionMemberDto loginMember = (SessionMemberDto) httpSession.getAttribute("loginMember");
        if (loginMember == null) {
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        List<AnalysisResult> results = analysisResultRepository.findAllByMemberId(loginMember.getId());

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("yy.MM.dd");

        Map<String, Long> dayCountMap = results.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getCreatedAt().toLocalDate().format(dayFormatter),
                        Collectors.counting()
                ));

        List<CalendarDayCountDto> dayCounts = dayCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new CalendarDayCountDto(e.getKey(), e.getValue()))
                .toList();

        Map<String, List<AnalysisResult>> distortionGroup = results.stream()
                .filter(r -> r.getDistortion() != null)
                .collect(Collectors.groupingBy(r -> r.getDistortion().getTag()));

        int totalDistortionCount = distortionGroup.values().stream()
                .mapToInt(List::size)
                .sum();

        List<DistortionStatDto> distortionStats = distortionGroup.entrySet().stream()
                .map(entry -> {
                    String tag = entry.getKey();
                    List<AnalysisResult> grouped = entry.getValue();
                    String typeName = grouped.get(0).getDistortion().getTypeName();
                    long count = grouped.size();
                    int percentage = totalDistortionCount == 0 ? 0 : (int) Math.round((count * 100.0) / totalDistortionCount);

                    return new DistortionStatDto(tag, typeName, count, percentage);
                })
                .sorted(Comparator.comparingLong(DistortionStatDto::getCount).reversed())
                .toList();

        List<RecentRecordDto> recentRecords = results.stream()
                .limit(10)
                .map(r -> RecentRecordDto.builder()
                        .sessionId(r.getSession().getId())
                        .stockName(
                                r.getSession().getStock() != null
                                        ? r.getSession().getStock().getName()
                                        : r.getSession().getCustomStockName()
                        )
                        .emotionLabel(
                                r.getDistortion() != null ? r.getDistortion().getTypeName() : null
                        )
                        .distortionTypeName(
                                r.getDistortion() != null ? r.getDistortion().getTypeName() : null
                        )
                        .sessionMode(r.getSession().getSessionMode())
                        .reflectionSummary(r.getReflectionSummary())
                        .createdDate(r.getCreatedAt().format(shortFormatter))
                        .build())
                .toList();

        return CalendarSummaryResponseDto.builder()
                .dayCounts(dayCounts)
                .distortionStats(distortionStats)
                .recentRecords(recentRecords)
                .build();
    }
}