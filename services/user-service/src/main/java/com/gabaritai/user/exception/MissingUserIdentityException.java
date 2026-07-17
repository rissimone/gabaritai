package com.gabaritai.user.exception;

import org.springframework.http.HttpStatus;

/** O API Gateway sempre envia X-User-Id em rotas protegidas; sua ausencia indica uso indevido. */
public class MissingUserIdentityException extends UserDomainException {

    public MissingUserIdentityException() {
        super(HttpStatus.UNAUTHORIZED, "MISSING_USER_IDENTITY", "Identidade do usuario nao informada.");
    }
}
