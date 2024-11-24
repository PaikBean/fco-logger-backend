package com.fconlinelogger.service;

import com.fconlinelogger.domain.FCOUser;
import com.fconlinelogger.dto.UserBasicInfoDto;
import com.fconlinelogger.dto.UserDto;
import com.fconlinelogger.dto.UserOuidDto;
import com.fconlinelogger.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
                .build();
    }

    @Transactional
    public void createUser(String nickName){
        UserOuidDto userOuid = nexonOpenAPICallService.searchUserName(nickName);
        UserBasicInfoDto userInfo = nexonOpenAPICallService.searchUserBasicInfo(userOuid.getOuid());

        userRepository.save(
                FCOUser.builder()
                        .ouid(userOuid.getOuid())
                        .nickname(userInfo.getNickname())
                        .level(userInfo.getLevel())
                        .build()
        );
    }
}
