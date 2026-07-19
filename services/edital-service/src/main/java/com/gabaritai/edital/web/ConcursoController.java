package com.gabaritai.edital.web;

import com.gabaritai.edital.domain.Concurso;
import com.gabaritai.edital.domain.ConcursoStatus;
import com.gabaritai.edital.exception.MissingUserIdentityException;
import com.gabaritai.edital.service.ConcursoService;
import com.gabaritai.edital.web.dto.ConcursoResponse;
import com.gabaritai.edital.web.dto.CriarConcursoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * UC-006 — Cadastrar concurso; UC-007 — Visualizar concursos cadastrados (RF03).
 * Identidade do candidato via header X-User-Id, propagado pelo API Gateway apos validar o
 * JWT (RNF-009) — este servico nao fica exposto publicamente (RNF-018/019).
 */
@RestController
@RequestMapping("/api/v1/concursos")
@Tag(name = "Concursos", description = "Cadastro e consulta de concursos de interesse do candidato")
public class ConcursoController {

    private static final String USER_ID_HEADER = "X-User-Id";

    private final ConcursoService concursoService;

    public ConcursoController(ConcursoService concursoService) {
        this.concursoService = concursoService;
    }

    @Operation(summary = "Cadastrar um novo concurso (UC-006)")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ConcursoResponse criarConcurso(
            @Parameter(hidden = true) @RequestHeader(value = USER_ID_HEADER, required = false) String userIdHeader,
            @Valid @RequestBody CriarConcursoRequest request) {
        Concurso concurso = concursoService.criarConcurso(
                parseUserId(userIdHeader),
                request.orgao(),
                request.bancaOrganizadora(),
                request.cargo(),
                request.numeroEdital(),
                request.dataProva(),
                request.escolaridade(),
                request.disciplinas(),
                request.quantidadeVagas(),
                request.linkOficial(),
                request.status());
        return ConcursoResponse.from(concurso);
    }

    @Operation(summary = "Listar os concursos do candidato autenticado, opcionalmente filtrando por situacao (UC-007)")
    @GetMapping
    public List<ConcursoResponse> listarConcursos(
            @Parameter(hidden = true) @RequestHeader(value = USER_ID_HEADER, required = false) String userIdHeader,
            @RequestParam(required = false) ConcursoStatus status) {
        return concursoService.listarConcursos(parseUserId(userIdHeader), status).stream()
                .map(ConcursoResponse::from)
                .toList();
    }

    @Operation(summary = "Consultar um concurso especifico do candidato autenticado (UC-007)")
    @GetMapping("/{id}")
    public ConcursoResponse buscarConcurso(
            @Parameter(hidden = true) @RequestHeader(value = USER_ID_HEADER, required = false) String userIdHeader,
            @PathVariable UUID id) {
        return ConcursoResponse.from(concursoService.buscarConcurso(parseUserId(userIdHeader), id));
    }

    private UUID parseUserId(String userIdHeader) {
        try {
            return UUID.fromString(userIdHeader);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new MissingUserIdentityException();
        }
    }
}
