package com.fconlinelogger.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class UserDto {
    private String ouid;
    private String nickname;
    private int level;
    private LocalDateTime lastModified;

    private List<MatchSummaryDto> matchSummaryDtoList;
}
