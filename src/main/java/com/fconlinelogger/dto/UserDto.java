package com.fconlinelogger.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserDto {
    private String nickname;
    private int level;
    private LocalDateTime lastModified;
}
