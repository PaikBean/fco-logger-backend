package com.fconlinelogger.service;

import com.fconlinelogger.dto.NexonErrorResponse;
import com.fconlinelogger.exception.NexonApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NexonOpenAPICallService {
    private final WebClient.Builder webClientBuilder;

    @Value("${nexon.api.base-url}")
    private String baseUrl;

    @Value("${nexon.api.api-key}")
    private String apiKey;

    public String searchUserName(String nickName) {
        log.info("Base URL: {}", baseUrl);
        log.info("API Key: {}", apiKey);

        try {
            String response = webClientBuilder
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
                                    .map(error -> {
                                        log.error("4xx 에러: {} - {}", error.getError().getName(), error.getError().getMessage());
                                        throw new NexonApiException(error.getError().getName(), error.getError().getMessage());
                                    }))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            clientResponse.bodyToMono(NexonErrorResponse.class)
                                    .map(error -> {
                                        log.error("5xx 에러: {} - {}", error.getError().getName(), error.getError().getMessage());
                                        throw new NexonApiException(error.getError().getName(), error.getError().getMessage());
                                    }))
                    .bodyToMono(String.class)
                    .block();

            log.info("API Response: {}", response);
            return response;

        } catch (WebClientResponseException e) {
            log.error("HTTP 상태 코드 에러: {} - {}", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("HTTP 상태 코드 에러", e);
        } catch (Exception e) {
            log.error("API 호출 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("API 호출 중 오류 발생", e);
        }
    }

}
