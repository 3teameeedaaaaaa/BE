package com.example.water.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SessionStatus {
    ONGOING(""),
    COMPLETED(""),
    ABORTED("");

    private final String text;
}
