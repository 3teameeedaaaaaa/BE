package com.example.water.domain.stock.controller;

import com.example.water.domain.stock.dto.StockSearchResponseDto;
import com.example.water.domain.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/search")
    public List<StockSearchResponseDto> searchStocks(@RequestParam String keyword) {
        return stockService.searchStocks(keyword);
    }
}