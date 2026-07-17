package com.gabaritai.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AuthDomainException {

    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "E-mail ou senha invalidos.");
    }
}
