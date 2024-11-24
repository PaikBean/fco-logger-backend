package com.fconlinelogger.service;

import com.fconlinelogger.domain.FCOUser;
import com.fconlinelogger.dto.nexon.MatchType;
import com.fconlinelogger.dto.nexon.UserBasicDto;
import com.fconlinelogger.dto.UserDto;
import com.fconlinelogger.dto.nexon.UserOuidDto;
import com.fconlinelogger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return UserDto.builder()
                .nickname(user.getNickname())
                .level(user.getLevel())
                .lastModified(user.getLastModified())
                .matchIdDtoList(nexonOpenAPICallService.searchUserMatchList(user.getOuid(), MatchType.OFFICIAL_MATCH, 0, 10))
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
