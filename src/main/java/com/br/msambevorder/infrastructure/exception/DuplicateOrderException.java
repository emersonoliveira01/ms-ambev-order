package com.br.msambevorder.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class DuplicateOrderException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public DuplicateOrderException(final HttpStatus status,
                                   final String errorCode,
                                   final String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
