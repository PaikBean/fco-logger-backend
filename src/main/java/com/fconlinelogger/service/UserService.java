package com.fconlinelogger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final NexonOpenAPICallService nexonOpenAPICallService;
    public void searchUser(String nickName) {
        nexonOpenAPICallService.searchUserName(nickName);
    }
}
