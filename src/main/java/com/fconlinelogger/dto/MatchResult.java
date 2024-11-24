package com.fconlinelogger.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchResult {
    WIN("승"),
    DRAW("무"),
    LOSE("패");

    private final String koreanLabel;
}