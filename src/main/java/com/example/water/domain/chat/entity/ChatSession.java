package com.example.water.domain.chat.entity;

import com.example.water.domain.member.entity.Member;
import com.example.water.domain.stock.entity.Stock;
import com.example.water.global.common.BaseEntity;
import com.example.water.global.common.SessionMode;
import com.example.water.global.common.SessionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatSession extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private String customStockName;

    @Enumerated(EnumType.STRING)
    private SessionMode sessionMode;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.ONGOING;
}
