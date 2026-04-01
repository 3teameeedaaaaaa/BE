package com.example.water.domain.chat.entity;

import com.example.water.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private ChatSession session;

    @Enumerated(EnumType.STRING)
    private SenderType senderType; // USER, AI (Enum 생성 필요)

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean isSkipped = false; // Q4 건너뛰기 여부
}
