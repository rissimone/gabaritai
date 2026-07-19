package com.gabaritai.edital.exception;

import org.springframework.http.HttpStatus;

public abstract class EditalDomainException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorCode;

    protected EditalDomainException(HttpStatus httpStatus, String errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
