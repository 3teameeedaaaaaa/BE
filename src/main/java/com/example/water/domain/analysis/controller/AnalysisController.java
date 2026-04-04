package com.example.water.domain.analysis.controller;

import com.example.water.domain.analysis.dto.AnalysisResultResponseDto;
import com.example.water.domain.analysis.dto.UserDecisionRequestDto;
import com.example.water.domain.analysis.service.AnalysisService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis/sessions")
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/{sessionId}")
    public ResponseEntity<AnalysisResultResponseDto> createAnalysisResult(
            @PathVariable Long sessionId,
            HttpSession session
    ) {
        return ResponseEntity.ok(analysisService.createAnalysisResult(sessionId, session));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<AnalysisResultResponseDto> getAnalysisResult(
            @PathVariable Long sessionId,
            HttpSession session
    ) {
        return ResponseEntity.ok(analysisService.getAnalysisResult(sessionId, session));
    }

    @PatchMapping("/{sessionId}/decision")
    public ResponseEntity<Void> updateUserDecision(
            @PathVariable Long sessionId,
            @Valid @RequestBody UserDecisionRequestDto request,
            HttpSession session
    ) {
        analysisService.updateUserDecision(sessionId, request.getUserDecision(), session);
        return ResponseEntity.ok().build();
    }
}