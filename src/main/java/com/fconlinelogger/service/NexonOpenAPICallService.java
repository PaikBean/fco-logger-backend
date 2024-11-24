package com.fconlinelogger.service;

import com.fconlinelogger.dto.NexonErrorResponse;
import com.fconlinelogger.dto.UserBasicInfoDto;
import com.fconlinelogger.dto.UserOuidDto;
import com.fconlinelogger.exception.NexonApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class NexonOpenAPICallService {
    private final WebClient.Builder webClientBuilder;

    @Value("${nexon.api.base-url}")
    private String baseUrl;

    @Value("${nexon.api.api-key}")
    private String apiKey;

    /**
     * 계정 식별자(ouid)를 조회합니다.
     * @param nickName 닉네임
     * @return
     */
    public UserOuidDto searchUserName(String nickName) {
        try {
            return webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/fconline/v1/id")
                            .queryParam("nickname", nickName)
                            .build())
                    .header("accept", "application/json")
                    .header("x-nxopen-api-key", apiKey)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            clientResponse.bodyToMono(NexonErrorResponse.class)
                                    .handle((error, sink) ->
                                            sink.error(new NexonApiException(error.getError().getName(), error.getError().getMessage()))
                                    ))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            clientResponse.bodyToMono(NexonErrorResponse.class)
                                    .handle((error, sink) ->
                                        sink.error(new NexonApiException(error.getError().getName(), error.getError().getMessage()))
                                    ))
                    .bodyToMono(UserOuidDto.class)
                    .block();
        } catch (NexonApiException e) {
            log.error("Nexon API Error: {}", e.getMessage(), e);
            throw new NexonApiException("API 호출 중 오류 발생", e.getMessage());
        } catch (Exception e) {
            log.error("Nexon API 호출 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("API 호출 중 오류 발생", e);
        }
    }

    /**
     * 기본 정보를 조회합니다.
     * @param ouid 계정식별자
     * @return
     */
    public UserBasicInfoDto searchUserBasicInfo(String ouid) {
        try {
            return webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/fconline/v1/user/basic")
                            .queryParam("ouid", ouid)
                            .build())
                    .header("accept", "application/json")
                    .header("x-nxopen-api-key", apiKey)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            clientResponse.bodyToMono(NexonErrorResponse.class)
                                    .handle((error, sink) ->
                                        sink.error(new NexonApiException(error.getError().getName(), error.getError().getMessage()))
                                    ))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            clientResponse.bodyToMono(NexonErrorResponse.class)
                                    .handle((error, sink) ->
                                        sink.error(new NexonApiException(error.getError().getName(), error.getError().getMessage()))
                                    ))
                    .bodyToMono(UserBasicInfoDto.class)
                    .block();

        } catch (NexonApiException e) {
            log.error("Nexon API Error: {}", e.getMessage(), e);
            throw new NexonApiException("API 호출 중 오류 발생", e.getMessage());
        } catch (Exception e) {
            log.error("Nexon API 호출 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("API 호출 중 오류 발생", e);
        }
    }

}
