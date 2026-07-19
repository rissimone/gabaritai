package com.gabaritai.edital.exception;

import org.springframework.http.HttpStatus;

public class ConcursoNaoEncontradoException extends EditalDomainException {

    public ConcursoNaoEncontradoException() {
        super(HttpStatus.NOT_FOUND, "CONCURSO_NAO_ENCONTRADO", "Concurso nao encontrado.");
    }
}
