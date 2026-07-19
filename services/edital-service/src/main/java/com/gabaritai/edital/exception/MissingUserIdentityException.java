package com.gabaritai.edital.exception;

import org.springframework.http.HttpStatus;

/** O API Gateway sempre envia X-User-Id em rotas protegidas; sua ausencia indica uso indevido. */
public class MissingUserIdentityException extends EditalDomainException {

    public MissingUserIdentityException() {
        super(HttpStatus.UNAUTHORIZED, "MISSING_USER_IDENTITY", "Identidade do usuario nao informada.");
    }
}
