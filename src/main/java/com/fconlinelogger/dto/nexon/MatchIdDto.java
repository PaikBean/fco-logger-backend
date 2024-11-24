package com.fconlinelogger.dto.nexon;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MatchIdDto {
    private String matchId; // 매치 고유 식별자
}
