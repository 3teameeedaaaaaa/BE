package com.example.water.domain.stock.repository;

import com.example.water.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findTop5ByNameStartingWithOrderByNameAsc(String keyword);
}
