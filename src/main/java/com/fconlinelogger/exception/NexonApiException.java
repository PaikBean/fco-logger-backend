package com.fconlinelogger.exception;

public class NexonApiException extends RuntimeException {
    private final String errorName;

    public NexonApiException(String errorName, String message) {
        super(message);
        this.errorName = errorName;
    }

    public String getErrorName() {
        return errorName;
    }
}
