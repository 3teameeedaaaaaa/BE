package com.example.water.domain.chat.dto;

import com.example.water.domain.chat.entity.SenderType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisChatMessageDto implements Serializable {

    private Integer sequence;
    private SenderType senderType;

    private String emotion;
    private String singleChip;
    private String commonChip;

    private String content;
    private LocalDateTime createdAt;
}