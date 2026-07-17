package com.gabaritai.auth.exception;

import org.springframework.http.HttpStatus;

public class AccountBlockedException extends AuthDomainException {

    public AccountBlockedException() {
        super(HttpStatus.FORBIDDEN, "ACCOUNT_BLOCKED", "Esta conta foi bloqueada. Entre em contato com o suporte.");
    }
}
