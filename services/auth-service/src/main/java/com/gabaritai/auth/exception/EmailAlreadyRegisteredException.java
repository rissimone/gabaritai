package com.gabaritai.auth.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyRegisteredException extends AuthDomainException {

    public EmailAlreadyRegisteredException() {
        super(HttpStatus.CONFLICT, "EMAIL_ALREADY_REGISTERED", "Ja existe uma conta cadastrada com este e-mail.");
    }
}
