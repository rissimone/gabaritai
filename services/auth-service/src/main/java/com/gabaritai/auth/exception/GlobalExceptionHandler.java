package com.gabaritai.auth.exception;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduz excecoes de dominio e de validacao para respostas RFC 7807 (RNF-122):
 * mensagens compreensiveis ao usuario, sem detalhes tecnicos, com identificador para suporte.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthDomainException.class)
    public ProblemDetail handleAuthDomainException(AuthDomainException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getHttpStatus(), ex.getMessage());
        problem.setProperty("errorCode", ex.getErrorCode());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Um ou mais campos sao invalidos.");
        problem.setProperty("errorCode", "VALIDATION_ERROR");
        problem.setProperty("fields", ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList());
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleMalformedRequest(HttpMessageNotReadableException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Corpo da requisicao invalido ou malformado.");
        problem.setProperty("errorCode", "MALFORMED_REQUEST");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception ex) {
        String correlationId = UUID.randomUUID().toString();
        log.error("Erro inesperado [correlationId={}]", correlationId, ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado. Informe o identificador ao suporte.");
        problem.setProperty("errorCode", "INTERNAL_ERROR");
        problem.setProperty("correlationId", correlationId);
        return problem;
    }
}
