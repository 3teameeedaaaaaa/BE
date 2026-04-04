package com.example.water.domain.stock.controller;

import com.example.water.domain.stock.dto.StockSearchResponseDto;
import com.example.water.domain.stock.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(name = "Stock", description = "주식 관련 API")
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(summary = "주식 검색", description = "주식 검색 최대 5개 뜨게 해놓음 API 단어 입력마다 호출")
    @GetMapping("/search")
    public List<StockSearchResponseDto> searchStocks(@RequestParam String keyword) {
        return stockService.searchStocks(keyword);
    }
}