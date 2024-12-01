package com.fconlinelogger.service;

import com.fconlinelogger.domain.FCOUser;
import com.fconlinelogger.domain.MatchSummary;
import com.fconlinelogger.dto.MatchSummaryDto;
import com.fconlinelogger.dto.nexon.MatchEndType;
import com.fconlinelogger.dto.nexon.MatchResult;
import com.fconlinelogger.dto.nexon.MatchType;
import com.fconlinelogger.dto.nexon.match.MatchDto;
import com.fconlinelogger.dto.nexon.user.MatchIdDto;
import com.fconlinelogger.dto.nexon.user.UserBasicDto;
import com.fconlinelogger.dto.UserDto;
import com.fconlinelogger.dto.nexon.user.UserOuidDto;
import com.fconlinelogger.repository.MatchSummaryRepository;
import com.fconlinelogger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final NexonOpenAPICallService nexonOpenAPICallService;
    private final UserRepository userRepository;
    private final MatchSummaryRepository matchSummaryRepository;

    public UserDto searchUser(String nickName) {

        if(!userRepository.existsFCOUserByNickname(nickName)){
            return null;
        }

        FCOUser user = userRepository.findByNickname(nickName);

        List<MatchSummaryDto> matchSummaryDtoList = new ArrayList<>();

        for (MatchSummary matchSummary : matchSummaryRepository.findAllByUser(user)) {
            matchSummaryDtoList.add(
                    MatchSummaryDto.builder()
                            .matchId(matchSummary.getMatchId())
                            .matchResult(matchSummary.getMatchResult().getDescription())
                            .myGoal(matchSummary.getMyGoal())
                            .opponentGoal(matchSummary.getOpponentGoal())
                            .opponentNickname(matchSummary.getOpponentNickname())
                            .matchType(matchSummary.getMatchType().getDescription())
                            .matchTime(matchSummary.getMatchDate())
                            .build()
            );
        }


        return UserDto.builder()
                .ouid(user.getOuid())
                .nickname(user.getNickname())
                .level(user.getLevel())
                .lastModified(user.getLastModified())
                .matchSummaryDtoList(matchSummaryDtoList)
                .build();
    }

    @Transactional
    public void createUser(String nickName){
        UserOuidDto userOuid = nexonOpenAPICallService.searchUserName(nickName);
        UserBasicDto userInfo = nexonOpenAPICallService.searchUserBasicInfo(userOuid.getOuid());

        userRepository.save(
                FCOUser.builder()
                        .ouid(userOuid.getOuid())
                        .nickname(userInfo.getNickname())
                        .level(userInfo.getLevel())
                        .build()
        );
    }

    @Transactional
    public void createMatchSummary(String nickname){
        FCOUser user = userRepository.findByNickname(nickname);
        List<MatchIdDto> matchIdDtoList = nexonOpenAPICallService.searchUserMatchList(user.getOuid(), MatchType.OFFICIAL_MATCH, 0, 10);

        for(MatchIdDto matchIdDto : matchIdDtoList){
            nexonOpenAPICallService.searchMatchDetail(matchIdDto.getMatchId());
        }

        for(MatchIdDto matchIdDto : matchIdDtoList){
            MatchDto matchDto = nexonOpenAPICallService.searchMatchDetail(matchIdDto.getMatchId());
            int myInfo = matchDto.getMatchInfo().getFirst().getNickname().toLowerCase().equals(user.getNickname()) ? 0 : 1;
            int opponentInfo = myInfo == 0 ? 1 : 0;

            matchSummaryRepository.save(MatchSummary.builder()
                    .matchId(matchIdDto.getMatchId())
                            .user(user)
                            .matchResult(MatchResult.fromDescription(matchDto.getMatchInfo().get(myInfo).getMatchDetail().getMatchResult()))
                            .matchEndType(MatchEndType.fromCode(matchDto.getMatchInfo().get(myInfo).getMatchDetail().getMatchEndType()))
                            .myGoal(matchDto.getMatchInfo().get(myInfo).getShoot().getGoalTotal())
                            .opponentGoal(matchDto.getMatchInfo().get(opponentInfo).getShoot().getGoalTotal())
                            .opponentNickname(matchDto.getMatchInfo().get(opponentInfo).getNickname())
                            .matchType(MatchType.fromCode(matchDto.getMatchType()))
                            .matchDate(LocalDateTime.parse(matchDto.getMatchDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build()
            );
        }
    }
}
