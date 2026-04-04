package com.example.water.domain.analysis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.ArrayList;

/**
 * 프론트엔드로부터 전달받는 AI 채팅 요청 객체입   니다.
 * 이 객체는 컨트롤러에서 가공되어 규태님의 AI 서버로 전달됩니다.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AnalysisAiRequestDto {

    // 1. 서비스 모드 및 종목 정보
    private String mode;                     // "pre"(사전 결정) 또는 "post"(사후 복기)
    private String ticker;                   // 분석 대상 종목명 (예: "삼성전자", "NVDA")
    private String emotion;                  // 사용자가 선택한 현재 감정 상태 (예: "불안해요", "조급해요")

    // 2. 화면상의 선택 칩 데이터 (SCR02 단계)
    private String situation;                // 상황 선택 칩 (예: "급등 중", "손실 중")
    private String singleChip;               // 추가 상황 칩 (예: "뉴스를 봤어요", "주변 얘기를 들었어요")

    // 3. 사용자 입력 데이터
    private String text;                     // 사용자가 직접 입력창에 타이핑한 추가 메시지 (없으면 빈 값)

    // 4. 대화 상태 제어 데이터
    private Integer turn_number = 1;         // 현재 대화의 순서 (첫 질문 시 무조건 1)
    private String previous_distortion_type; // 1턴 응답에서 감지된 인지왜곡 유형 (2턴 호출 시 필수 전달)

    // 5. 대화 맥락 유지 (Memory)
    // 영섭님이 세션 스토리지에 저장해둔 이전 대화 내역들이 리스트로 들어옵니다.
    private List<MessageDto> conversation_history = new ArrayList<>();

    /**
     * 대화 내역의 한 단위를 정의하는 내부 클래스입니다.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class MessageDto {
        private String role;    // 화자 구분: "user"(사용자) 또는 "assistant"(AI)
        private String content; // 실제 대화 내용
    }
}