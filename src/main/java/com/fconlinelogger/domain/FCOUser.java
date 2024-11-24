package com.fconlinelogger.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "fco_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FCOUser {
    @Id
    @Column(name = "ouid", nullable = false, unique = true)
    private String ouid; // 계정 식별자

    @Column(name = "nickname", nullable = false)
    private String nickname; // 닉네임

    @Column(name = "level", nullable = false)
    private Integer level; // 레벨

    @Column(name = "last_modified", nullable = false)
    private LocalDateTime lastModified; // 최종 갱신일

    @Builder
    public FCOUser(String ouid, String nickname, Integer level) {
        this.ouid = ouid;
        this.nickname = nickname.toLowerCase();
        this.level = level;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastModified = LocalDateTime.now(); // 데이터 저장 또는 업데이트 시 현재 시간으로 설정
    }
}
