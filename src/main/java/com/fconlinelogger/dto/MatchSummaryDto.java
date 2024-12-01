package com.fconlinelogger.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MatchSummaryDto {
    private String matchId;
    private String matchResult; // 승/무/패 ("승", "무", "패")
    private int myGoal;
    private int opponentGoal;
    private String opponentNickname; // 상대 닉네임
    private String matchType; // 경기 타입 (예: "리그 친선", "공식 경기" 등)
    private LocalDateTime matchTime; // 경기 시간 (예: "2023-11-24 19:30")
}