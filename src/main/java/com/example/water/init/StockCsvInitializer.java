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
import java.util.ArrayList;
import java.util.List;

@Component
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
                Reader reader = new InputStreamReader(resource.getInputStream(), "MS949");
                CSVParser parser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build()
                        .parse(reader)
        ) {
            System.out.println("CSV headers = " + parser.getHeaderMap().keySet());

            List<Stock> stocks = new ArrayList<>();

            for (CSVRecord record : parser) {
                String tickerCode = record.get("단축코드").trim();
                String name = record.get("한글 종목명").trim();
                String marketType = record.get("시장구분").trim();

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
            System.out.println("주식 CSV 적재 완료: " + stocks.size() + "건");
        }
    }
}