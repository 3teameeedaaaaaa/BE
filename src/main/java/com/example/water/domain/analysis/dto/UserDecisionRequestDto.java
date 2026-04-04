package com.example.water.domain.analysis.dto;

import com.example.water.global.common.UserDecision;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDecisionRequestDto {

    @NotNull
    private UserDecision userDecision;
}