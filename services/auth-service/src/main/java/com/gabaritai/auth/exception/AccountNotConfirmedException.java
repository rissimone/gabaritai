package com.gabaritai.auth.exception;

import org.springframework.http.HttpStatus;

public class AccountNotConfirmedException extends AuthDomainException {

    public AccountNotConfirmedException() {
        super(HttpStatus.FORBIDDEN, "ACCOUNT_NOT_CONFIRMED",
                "Confirme seu e-mail antes de fazer login.");
    }
}
