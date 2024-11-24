package com.fconlinelogger.dto.nexon.match;

import lombok.Getter;

import java.util.List;

@Getter
public class MatchDto {
    private String matchId;         // 매치 고유 식별자
    private String matchDate;       // 매치 일자(UTC) ex : 2023-10-29T12:22:48
    private String matchType;       // 매치 종류
    private List<MatchInfoDto> matchInfo;
}
