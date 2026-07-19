package com.gabaritai.edital.web.dto;

import com.gabaritai.edital.domain.ConcursoStatus;
import com.gabaritai.edital.domain.EducationLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record CriarConcursoRequest(
        @NotBlank @Size(max = 200) String orgao,
        @NotBlank @Size(max = 200) String bancaOrganizadora,
        @NotBlank @Size(max = 200) String cargo,
        @Size(max = 50) String numeroEdital,
        LocalDate dataProva,
        EducationLevel escolaridade,
        List<String> disciplinas,
        @Positive Integer quantidadeVagas,
        @Size(max = 500) String linkOficial,
        ConcursoStatus status) {
}
