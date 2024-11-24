package com.fconlinelogger.dto.nexon.match;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShootDetailDto {
    private int goalTime; // 슛 시간
    private double x; // 슛 x좌표
    private double y; // 슛 y좌표
    private int type; // 슛 종류
    private int result; // 슛 결과
    private int spId; // 슈팅 선수 고유 식별자
    private int spGrade; // 슈팅 선수 강화 등급
    private int spLevel; // 슈팅 선수 레벨
    private boolean spIdType; // 슈팅 선수 임대 여부
    private boolean assist; // 어시스트 여부
    private int assistSpId; // 어시스트 선수 고유 식별자
    private double assistX; // 어시스트 x좌표
    private double assistY; // 어시스트 y좌표
    private boolean hitPost; // 골포스트 맞춤 여부
    private boolean inPenalty; // 페널티박스 안에서 넣은 슛 여부
}
