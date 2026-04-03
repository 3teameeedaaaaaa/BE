package com.example.water.domain.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockSearchResponseDto {
    private Long id;
    private String tickerCode;
    private String name;
    private String marketType;
}