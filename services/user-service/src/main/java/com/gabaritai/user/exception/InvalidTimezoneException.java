package com.gabaritai.user.exception;

import org.springframework.http.HttpStatus;

public class InvalidTimezoneException extends UserDomainException {

    public InvalidTimezoneException(String timezone) {
        super(HttpStatus.BAD_REQUEST, "INVALID_TIMEZONE", "Fuso horario invalido: " + timezone);
    }
}
