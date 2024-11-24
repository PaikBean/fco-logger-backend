package com.fconlinelogger.dto.nexon.match;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerDto {
    private int spId; // 선수 고유 식별자
    private int spPosition; // 선수 포지션
    private int spGrade; // 선수 강화 등급
    private PlayerStatusDto status; // 선수 경기 스탯
}
