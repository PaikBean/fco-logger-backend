package com.fconlinelogger.dto; // 패키지 선언: 파일 위치와 일치해야 합니다.

import lombok.Data;

@Data
public class NexonErrorResponse {
    private ErrorDetail error;

    @Data
    public static class ErrorDetail {
        private String name;
        private String message;
    }
}
