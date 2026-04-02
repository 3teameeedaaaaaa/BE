package com.example.water.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserDecision {
    WATCH("잠시 관망하겠습니다"),
    PROCEED("그래도 진행하겠습니다"),
    STOP("기록을 마칩니다");

    private final String text;
}
