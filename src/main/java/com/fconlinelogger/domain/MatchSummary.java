package com.fconlinelogger.domain;

import com.fconlinelogger.dto.nexon.MatchEndType;
import com.fconlinelogger.dto.nexon.MatchResult;
import com.fconlinelogger.dto.nexon.MatchType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "match_summary")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchSummary {
    @Id
    @Column(name = "match_id", nullable = false, unique = true)
    private String matchId; // 매치 고유 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ouid", nullable = false)
    private FCOUser user; // 계정 식별자 (외래 키)

    @Enumerated(EnumType.STRING)
    @Column(name = "match_result", nullable = false)
    private MatchResult matchResult; // 결과 (승/무/패)

    @Enumerated(EnumType.STRING)
    @Column(name = "match_end_type", nullable = false)
    private MatchEndType matchEndType; // 경기 종료 유형

    @Column(name = "my_goal", nullable = false)
    private int myGoal; // 내 골 수

    @Column(name = "opponent_goal", nullable = false)
    private int opponentGoal; // 상대 골 수

    @Column(name = "opponent_nickname")
    private String opponentNickname; // 상대 닉네임

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type", nullable = false)
    private MatchType matchType; // 경기 타입 (MatchType 코드)

    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate; // 경기 시간

    @Column(name = "registration_date")
    private LocalDateTime registrationDate; // DB 등록일

    @Builder
    public MatchSummary(String matchId, FCOUser user, MatchResult matchResult, MatchEndType matchEndType, int myGoal, int opponentGoal, String opponentNickname, MatchType matchType, LocalDateTime matchDate) {
        this.matchId = matchId;
        this.user = user;
        this.matchResult = matchResult;
        this.matchEndType = matchEndType;
        this.myGoal = myGoal;
        this.opponentGoal = opponentGoal;
        this.opponentNickname = opponentNickname;
        this.matchType = matchType;
        this.matchDate = matchDate;
    }

    @PrePersist
    protected void onUpdate() {
        this.registrationDate = LocalDateTime.now(); // 데이터 저장 또는 업데이트 시 현재 시간으로 설정
    }
}
