package com.fconlinelogger.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class UserDto {
    private String nickname;
    private int level;
    private LocalDateTime lastModified;

    private List<MatchIdDto> matchIdDtoList;
}
