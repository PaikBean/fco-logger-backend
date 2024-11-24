package com.fconlinelogger.dto.nexon.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchIdDto {
    private String matchId; // 매치 고유 식별자
}
