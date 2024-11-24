package com.fconlinelogger.dto.nexon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchResult {
    WIN("W", "승"),      // W: Win
    DRAW("D", "무"),     // D: Draw
    LOSE("L", "패");     // L: Lose

    private final String code;         // 결과 코드 (W/D/L)
    private final String description;  // 한글 설명

    public static MatchResult fromCode(String code) {
        for (MatchResult result : MatchResult.values()) {
            if (result.code.equalsIgnoreCase(code)) {
                return result;
            }
        }
        throw new IllegalArgumentException("Invalid MatchResult code: " + code);
    }
}
