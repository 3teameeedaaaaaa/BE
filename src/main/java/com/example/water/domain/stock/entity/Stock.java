package com.example.water.domain.stock.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stock", indexes = {
        @Index(name = "idx_ticker", columnList = "tickerCode"),
        @Index(name = "idx_name", columnList = "name")
})
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tickerCode;

    @Column(nullable = false)
    private String name;

    private String marketType; // KOSPI, KOSDAQ 등
}
