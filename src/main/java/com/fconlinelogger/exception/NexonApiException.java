package com.fconlinelogger.exception;

import lombok.Getter;

@Getter
public class NexonApiException extends RuntimeException {
    private final String errorName;

    public NexonApiException(String errorName, String message) {
        super(message);
        this.errorName = errorName;
    }
}
