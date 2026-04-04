package com.example.water.domain.board.controller;

import com.example.water.domain.board.dto.CalendarSummaryResponseDto;
import com.example.water.domain.board.service.CalendarAnalysisService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarAnalysisController {

    private final CalendarAnalysisService calendarAnalysisService;

    @GetMapping("/summary")
    public ResponseEntity<CalendarSummaryResponseDto> getCalendarSummary(HttpSession session) {
        return ResponseEntity.ok(calendarAnalysisService.getCalendarSummary(session));
    }
}