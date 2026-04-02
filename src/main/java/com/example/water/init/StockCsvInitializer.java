package com.example.water.init;

import com.example.water.domain.stock.entity.Stock;
import com.example.water.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//@Component
@RequiredArgsConstructor
public class StockCsvInitializer implements CommandLineRunner {

    private final StockRepository stockRepository;

    @Override
    public void run(String... args) throws Exception {
        if (stockRepository.count() > 0) {
            return;
        }

        ClassPathResource resource = new ClassPathResource("data/stocks_kr.csv");

        try (
                Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                CSVParser parser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build()
                        .parse(reader)
        ) {
            List<Stock> stocks = new ArrayList<>();

            for (CSVRecord record : parser) {
                String tickerCode = record.get("Issue code").trim();
                String name = record.get("Issue name").trim();
                String marketType = record.get("Market type").trim();

                if (tickerCode.isEmpty() || name.isEmpty()) {
                    continue;
                }

                Stock stock = new Stock();
                stock.setTickerCode(tickerCode);
                stock.setName(name);
                stock.setMarketType(marketType);
                stocks.add(stock);
            }

            stockRepository.saveAll(stocks);
        }
    }
}
