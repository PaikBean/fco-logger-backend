package com.fconlinelogger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponse<T> {
    private int responseCode;       // 응답 코드 (예: 200, 400, 500)
    private T responseData;         // 실제 데이터
    private String responseMessage; // 응답 메시지
}
