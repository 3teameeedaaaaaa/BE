package com.example.water.domain.stock.service;

import com.example.water.domain.stock.dto.StockSearchResponseDto;
import com.example.water.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<StockSearchResponseDto> searchStocks(String keyword) {
        return stockRepository.findTop5ByNameStartingWithOrderByNameAsc(keyword).stream()
                .map(stock -> new StockSearchResponseDto(
                        stock.getId(),
                        stock.getTickerCode(),
                        stock.getName(),
                        stock.getMarketType()
                ))
                .toList();
    }
}