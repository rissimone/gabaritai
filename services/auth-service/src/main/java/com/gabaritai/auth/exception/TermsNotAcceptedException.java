package com.gabaritai.auth.exception;

import org.springframework.http.HttpStatus;

public class TermsNotAcceptedException extends AuthDomainException {

    public TermsNotAcceptedException() {
        super(HttpStatus.BAD_REQUEST, "TERMS_NOT_ACCEPTED",
                "E necessario aceitar os termos de uso e a politica de privacidade.");
    }
}
