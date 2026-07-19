package com.gabaritai.edital.exception;

import org.springframework.http.HttpStatus;

/** UC-006, fluxo alternativo: "o concurso ja existe na plataforma" (para este usuario). */
public class ConcursoDuplicadoException extends EditalDomainException {

    public ConcursoDuplicadoException() {
        super(HttpStatus.CONFLICT, "CONCURSO_DUPLICADO",
                "Voce ja tem um concurso cadastrado com este orgao, cargo e banca.");
    }
}
