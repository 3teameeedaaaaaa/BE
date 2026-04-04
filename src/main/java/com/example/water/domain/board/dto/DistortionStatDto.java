package com.example.water.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DistortionStatDto {
    private String tag;
    private String typeName;
    private Long count;
    private Integer percentage;
}