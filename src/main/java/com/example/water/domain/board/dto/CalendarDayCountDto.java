package com.example.water.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalendarDayCountDto {
    private String date;
    private Long count;
}