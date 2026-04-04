package com.example.water.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SessionMode{
    PRE("",""),
    POST("","");

    private final String code;
    private final String text;
}
