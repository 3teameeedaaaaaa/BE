package com.example.water.domain.analysis.entity;

import com.example.water.domain.chat.entity.ChatSession;
import com.example.water.global.common.BaseEntity;
import com.example.water.global.common.UserDecision;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AnalysisResult extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private ChatSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distortion_id")
    private CognitiveDistortion distortion;

    @Column(columnDefinition = "TEXT")
    private String reflectionSummary;

    @Enumerated(EnumType.STRING)
    private UserDecision userDecision; // WATCH, PROCEED, STOP
}
