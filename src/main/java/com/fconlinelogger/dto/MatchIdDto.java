package com.fconlinelogger.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MatchIdDto {
    private String matchId; // 매치 고유 식별자
}
