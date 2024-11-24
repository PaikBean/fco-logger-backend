package com.fconlinelogger.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchType {
    LEAGUE_FRIENDLY(30, "리그친선"),
    CLASSIC_1ON1(40, "클래식 1on1"),
    OFFICIAL_MATCH(50, "공식경기"),
    MANAGER_MODE(52, "감독모드"),
    OFFICIAL_FRIENDLY(60, "공식친선");

    private final int code;
    private final String description;

    public static MatchType fromCode(int code) {
        for (MatchType type : MatchType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid MatchType code: " + code);
    }
}