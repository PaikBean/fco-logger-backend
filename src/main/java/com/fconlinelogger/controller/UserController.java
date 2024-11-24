package com.fconlinelogger.controller;

import com.fconlinelogger.dto.CustomResponse;
import com.fconlinelogger.dto.UserDto;
import com.fconlinelogger.exception.NexonApiException;
import com.fconlinelogger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/searchUser/{nickName}")
    public ResponseEntity<CustomResponse<UserDto>> searchUser(@PathVariable("nickName") String nickName) {
        log.info("searchUser nickName : {}", nickName.toLowerCase());

        try {
            UserDto userDto = Optional.ofNullable(userService.searchUser(nickName.toLowerCase()))
                    .orElseGet(() -> {
                        userService.createUser(nickName.toLowerCase());
                        return userService.searchUser(nickName.toLowerCase());
                    });

            return ResponseEntity.ok(
                    CustomResponse.<UserDto>builder()
                            .responseCode(HttpStatus.OK.value())
                            .responseData(userDto)
                            .build()
            );
        } catch (NexonApiException e) {
            log.error("Nexon API error: {}", nickName, e);
            // 502 Bad Gateway: 외부 API 호출 실패
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(
                            CustomResponse.<UserDto>builder()
                                    .responseCode(HttpStatus.BAD_GATEWAY.value())
                                    .responseData(null)
                                    .responseMessage("Failed to communicate with Nexon API: " + e.getMessage())
                                    .build()
                    );
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing user: {}", nickName, e);
            // 500 Internal Server Error: 서버 오류
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            CustomResponse.<UserDto>builder()
                                    .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .responseData(null)
                                    .responseMessage("An unexpected error occurred: " + e.getMessage())
                                    .build()
                    );
        }
    }
}
