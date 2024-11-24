package com.fconlinelogger.service;

import com.fconlinelogger.dto.nexon.*;
import com.fconlinelogger.dto.nexon.match.MatchDto;
import com.fconlinelogger.dto.nexon.user.MatchIdDto;
import com.fconlinelogger.dto.nexon.user.UserBasicDto;
import com.fconlinelogger.dto.nexon.user.UserOuidDto;
import com.fconlinelogger.exception.NexonApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public UserBasicDto searchUserBasicInfo(String ouid) {
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
                    .bodyToMono(UserBasicDto.class)
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
     * 유저 매치 리스트를 조회합니다.
     *
     * @param ouid      계정 식별자
     * @param matchType 매치 타입 (MatchType enum)
     * @param offset    리스트에서 가져올 시작 위치
     * @param limit     리스트에서 가져올 개수 (최대 100개)
     * @return 매치 ID 리스트
     */
    public List<MatchIdDto> searchUserMatchList(String ouid, MatchType matchType, int offset, int limit) {
        try {
            List<String> matchIds = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/fconline/v1/user/match")
                            .queryParam("ouid", ouid)
                            .queryParam("matchtype", matchType.getCode()) // Enum의 코드 값 사용
                            .queryParam("offset", offset)
                            .queryParam("limit", limit)
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
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                    .block();

            return Optional.ofNullable(matchIds)
                    .orElse(Collections.emptyList()) // matchIds가 null이면 빈 리스트 반환
                    .stream()
                    .map(matchId -> MatchIdDto.builder().matchId(matchId).build())
                    .toList();

        } catch (NexonApiException e) {
            log.error("Nexon API Error: {}", e.getMessage(), e);
            throw new NexonApiException("API 호출 중 오류 발생", e.getMessage());
        } catch (Exception e) {
            log.error("Nexon API 호출 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("API 호출 중 오류 발생", e);
        }
    }

    /**
     * 매치 고유 식별자{matchid}로 매치의 상세 정보를 조회합니다.
     *
     * @param matchid 매치 고유 식별자
     * @return
     */
    public MatchDto searchMatchDetail(String matchid) {
        try {
            return webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/fconline/v1/match-detail")
                            .queryParam("matchid", matchid)
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
                    .bodyToMono(MatchDto.class)
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
