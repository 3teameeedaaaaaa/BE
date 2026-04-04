package com.example.water.domain.board.entity;

import com.example.water.domain.analysis.entity.AnalysisResult;
import com.example.water.domain.member.entity.Member;
import com.example.water.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AnonymousBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private AnalysisResult result; // 분석 결과 공유 시 연결하도록 설정함

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    private int likeCount = 0;
}
