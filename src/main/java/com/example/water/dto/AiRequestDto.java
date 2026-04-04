package com.example.water.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class AiRequestDto {
    private String mode;                     // "pre" 또는 "post"
    private String ticker;                   // "삼성전자"
    private String emotion;                  // "불안해요"
    private String situation;                // ★ 영섭님이 보낼 칩 데이터 (예: "급등 중이에요")
    private String text;                     // 사용자가 직접 타이핑한 추가 메시지 (없으면 빈 값)
    private String singleChip;               // 단일 선택칩
    private Integer turn_number = 1;         // 기본값 1
    private String previous_distortion_type; // 이전 왜곡 유형 (없으면 null)
    private List<MessageDto> conversation_history = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MessageDto {
        private String role;    // "user" 또는 "assistant"
        private String content;
    }
}