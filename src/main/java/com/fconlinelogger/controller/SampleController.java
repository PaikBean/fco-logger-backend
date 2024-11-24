package com.fconlinelogger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SampleController {

    @GetMapping("/test")
    public String testAPI(){
        log.info("TestAPI 입니다.");
        return "테스트 응답값!";
    }
}
