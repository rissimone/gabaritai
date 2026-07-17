package com.gabaritai.auth.exception;

import org.springframework.http.HttpStatus;

public class AccountLockedException extends AuthDomainException {

    public AccountLockedException() {
        super(HttpStatus.LOCKED, "ACCOUNT_LOCKED",
                "Conta temporariamente bloqueada apos multiplas tentativas de login invalidas.");
    }
}
