package com.fconlinelogger.dto.nexon.match;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class MatchInfoDto {
    private String ouid;        // 계정 식별자
    private String nickname;    // 닉네임
    private MatchDetailDto matchDetail;
    private ShootDto shoot;
    private List<ShootDetailDto> shootDetail;
    private PassDto pass;
    private DefenceDto defence;
    private List<PlayerDto> player;
}
