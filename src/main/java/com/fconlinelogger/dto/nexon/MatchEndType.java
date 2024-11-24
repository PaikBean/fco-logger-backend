package com.fconlinelogger.dto.nexon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum MatchEndType {
    NORMAL_END("0", "정상 종료"),
    WIN_BY_FORFEIT("1", "몰수승"),
    LOSE_BY_FORFEIT("2", "몰수패");

    private final String code;
    private final String description;

    public static MatchEndType fromCode(String code) {
        for (MatchEndType type : MatchEndType.values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid MatchEndType code: " + code);
    }
}
