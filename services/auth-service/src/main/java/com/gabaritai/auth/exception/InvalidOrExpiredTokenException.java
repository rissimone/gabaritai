package com.gabaritai.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidOrExpiredTokenException extends AuthDomainException {

    public InvalidOrExpiredTokenException() {
        super(HttpStatus.BAD_REQUEST, "INVALID_OR_EXPIRED_TOKEN", "Token invalido ou expirado.");
    }
}
