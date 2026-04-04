package com.example.water.domain.analysis.service;

import com.example.water.domain.analysis.dto.AnalysisAiRequestDto;
import com.example.water.domain.analysis.dto.AnalysisAiResponseDto;
import com.example.water.global.common.SessionMode;
import org.springframework.stereotype.Service;

@Service
public class AnalysisAiService {

    public AnalysisAiResponseDto analyze(AnalysisAiRequestDto request) {
        if (request.getSessionMode() == SessionMode.PRE) {
            return AnalysisAiResponseDto.builder()
                    .distortionTag("CATASTROPHIZING")
                    .reflectionSummary("하락 자체보다 더 큰 손실이 올 것 같다는 상상이 판단을 먼저 끌고 가는 모습이 보였어요.")
                    .togetherAgreePercent(75)
                    .togetherOtherPercent(25)
                    .togetherComment("손절하고 나서 다음 날 반등한 적이 있어서, 원칙부터 다시 확인했어요.")
                    .build();
        }

        return AnalysisAiResponseDto.builder()
                .distortionTag("URGENCY")
                .reflectionSummary("급등 흐름을 놓치고 싶지 않은 조급함이 근거보다 먼저 앞섰던 것으로 보여요.")
                .togetherAgreePercent(89)
                .togetherOtherPercent(21)
                .togetherComment("급등장에서 산 건 거의 다 비슷한 이유였고, 기록해두니 패턴이 보였어요.")
                .build();
    }
}