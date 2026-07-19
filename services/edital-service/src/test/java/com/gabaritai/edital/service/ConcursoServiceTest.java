package com.gabaritai.edital.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabaritai.edital.domain.Concurso;
import com.gabaritai.edital.domain.ConcursoStatus;
import com.gabaritai.edital.domain.EducationLevel;
import com.gabaritai.edital.exception.ConcursoDuplicadoException;
import com.gabaritai.edital.exception.ConcursoNaoEncontradoException;
import com.gabaritai.edital.repository.ConcursoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConcursoServiceTest {

    @Mock
    private ConcursoRepository concursoRepository;

    @InjectMocks
    private ConcursoService concursoService;

    @Test
    void criarConcurso_withNewData_savesAndReturnsConcurso() {
        UUID userId = UUID.randomUUID();
        when(concursoRepository.existsByUserIdAndOrgaoIgnoreCaseAndCargoIgnoreCaseAndBancaOrganizadoraIgnoreCase(
                userId, "Tribunal X", "Analista", "Banca Y")).thenReturn(false);
        when(concursoRepository.save(any(Concurso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Concurso result = concursoService.criarConcurso(
                userId,
                "Tribunal X",
                "Banca Y",
                "Analista",
                "001/2026",
                LocalDate.of(2026, 12, 1),
                EducationLevel.SUPERIOR,
                List.of("Direito Constitucional", "Portugues"),
                50,
                "https://example.com/edital",
                null);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getOrgao()).isEqualTo("Tribunal X");
        assertThat(result.getStatus()).isEqualTo(ConcursoStatus.PREVISTO);
        assertThat(result.getDisciplinas()).containsExactly("Direito Constitucional", "Portugues");
    }

    @Test
    void criarConcurso_withDuplicateData_throwsAndDoesNotSave() {
        UUID userId = UUID.randomUUID();
        when(concursoRepository.existsByUserIdAndOrgaoIgnoreCaseAndCargoIgnoreCaseAndBancaOrganizadoraIgnoreCase(
                userId, "Tribunal X", "Analista", "Banca Y")).thenReturn(true);

        assertThatThrownBy(() -> concursoService.criarConcurso(
                userId, "Tribunal X", "Banca Y", "Analista", null, null, null, List.of(), null, null, null))
                .isInstanceOf(ConcursoDuplicadoException.class);

        verify(concursoRepository, never()).save(any());
    }

    @Test
    void buscarConcurso_whenNotFound_throws() {
        UUID userId = UUID.randomUUID();
        UUID concursoId = UUID.randomUUID();
        when(concursoRepository.findByIdAndUserId(concursoId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> concursoService.buscarConcurso(userId, concursoId))
                .isInstanceOf(ConcursoNaoEncontradoException.class);
    }

    @Test
    void listarConcursos_withStatusFilter_delegatesToFilteredQuery() {
        UUID userId = UUID.randomUUID();
        when(concursoRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, ConcursoStatus.PREVISTO))
                .thenReturn(List.of());

        concursoService.listarConcursos(userId, ConcursoStatus.PREVISTO);

        verify(concursoRepository).findByUserIdAndStatusOrderByCreatedAtDesc(userId, ConcursoStatus.PREVISTO);
        verify(concursoRepository, never()).findByUserIdOrderByCreatedAtDesc(any());
    }
}
