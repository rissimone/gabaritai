package com.gabaritai.edital.web.dto;

import com.gabaritai.edital.domain.Concurso;
import com.gabaritai.edital.domain.ConcursoStatus;
import com.gabaritai.edital.domain.EducationLevel;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record ConcursoResponse(
        UUID id,
        String orgao,
        String bancaOrganizadora,
        String cargo,
        String numeroEdital,
        LocalDate dataProva,
        EducationLevel escolaridade,
        Set<String> disciplinas,
        Integer quantidadeVagas,
        String linkOficial,
        ConcursoStatus status,
        Instant createdAt,
        Instant updatedAt) {

    public static ConcursoResponse from(Concurso concurso) {
        return new ConcursoResponse(
                concurso.getId(),
                concurso.getOrgao(),
                concurso.getBancaOrganizadora(),
                concurso.getCargo(),
                concurso.getNumeroEdital(),
                concurso.getDataProva(),
                concurso.getEscolaridade(),
                concurso.getDisciplinas(),
                concurso.getQuantidadeVagas(),
                concurso.getLinkOficial(),
                concurso.getStatus(),
                concurso.getCreatedAt(),
                concurso.getUpdatedAt());
    }
}
