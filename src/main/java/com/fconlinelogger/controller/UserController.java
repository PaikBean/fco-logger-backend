package com.fconlinelogger.controller;

import com.fconlinelogger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/searchUser/{nickName}")
    public void searchUser(@PathVariable("nickName") String nickName) {
        log.info("searchUser nickName : {}", nickName);
        userService.searchUser(nickName);
    }
}
