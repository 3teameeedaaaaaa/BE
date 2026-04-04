package com.example.water.domain.analysis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 규태님(AI 서버)으로부터 받은 분석 결과를 담는 응답 객체입니다.
 * 이 데이터가 영섭님(FE)의 화면(말풍선, 상단 배너 등)에 그려집니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class AnalysisAiResponseDto {

    // 1. 인지왜곡 분석 결과 (상단 배너용)
    private String distortion_type;    // 왜곡 유형 코드 (예: "catastrophizing")
    private String distortion_tag;     // 화면에 표시될 태그명 (예: "최악을 상상하고 있어요")

    // 2. AI 답변 내용 (채팅 말풍선용)
    private String empathy;            // AI의 첫 번째 공감 텍스트
    private String question;           // AI가 사용자에게 던지는 분석 질문

    // 3. 화면 제어 플래그
    private String question_type;      // "exploratory"(1턴) 또는 "meta_cognition"(2턴)
    private boolean required;          // true일 경우 프론트에서 '건너뛸게요' 버튼 숨김

    // 4. 2턴 전용 데이터 (하단 이진 선택 버튼용)
    private String meta_question;      // 버튼 위에 뜰 안내 문구
    private List<String> meta_options; // 버튼에 들어갈 텍스트 리스트 (예: ["감정 때문", "근거 변화"])

    // 5. 최종 결과 요약 (기록 완료 시 사용)
    private String reflection_summary; // 전체 대화의 핵심 요약 내용
}