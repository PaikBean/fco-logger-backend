package com.fconlinelogger.service;

import com.fconlinelogger.domain.FCOUser;
import com.fconlinelogger.dto.MatchSummaryDto;
import com.fconlinelogger.dto.nexon.MatchType;
import com.fconlinelogger.dto.nexon.match.MatchDto;
import com.fconlinelogger.dto.nexon.match.MatchInfoDto;
import com.fconlinelogger.dto.nexon.user.MatchIdDto;
import com.fconlinelogger.dto.nexon.user.UserBasicDto;
import com.fconlinelogger.dto.UserDto;
import com.fconlinelogger.dto.nexon.user.UserOuidDto;
import com.fconlinelogger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final NexonOpenAPICallService nexonOpenAPICallService;
    private final UserRepository userRepository;

    public UserDto searchUser(String nickName) {

        if(!userRepository.existsFCOUserByNickname(nickName)){
            return null;
        }

        FCOUser user = userRepository.findByNickname(nickName);

        List<MatchIdDto> matchIdDtos = nexonOpenAPICallService.searchUserMatchList(user.getOuid(), MatchType.OFFICIAL_MATCH, 0, 10);

        for(MatchIdDto matchIdDto : matchIdDtos){
            nexonOpenAPICallService.searchMatchDetail(matchIdDto.getMatchId());
        }

        List<MatchSummaryDto> matchSummaryDtoList = new ArrayList<>();

        for(MatchIdDto matchIdDto : matchIdDtos){
            MatchDto matchDto = nexonOpenAPICallService.searchMatchDetail(matchIdDto.getMatchId());
            int myInfo = matchDto.getMatchInfo().getFirst().getNickname().toLowerCase().equals(nickName) ? 0 : 1;
            int opponentInfo = matchDto.getMatchInfo().getFirst().getNickname().toLowerCase().equals(nickName) ? 1 : 0;

            matchSummaryDtoList.add(MatchSummaryDto.builder()
                            .matchId(matchIdDto.getMatchId())
                            .matchTime(matchDto.getMatchDate())
                            .opponentNickname(matchDto.getMatchInfo().get(opponentInfo).getNickname())
                            .matchResult(matchDto.getMatchInfo().get(myInfo).getMatchDetail().getMatchResult())
                            .myGoal(matchDto.getMatchInfo().get(myInfo).getShoot().getGoalTotal())
                            .opponentGoal(matchDto.getMatchInfo().get(opponentInfo).getShoot().getGoalTotal())
                            .matchType(MatchType.fromCode(matchDto.getMatchType()).getDescription())
                    .build());
        }

        return UserDto.builder()
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
}
