package com.gabaritai.edital.service;

import com.gabaritai.edital.domain.Concurso;
import com.gabaritai.edital.domain.ConcursoStatus;
import com.gabaritai.edital.domain.EducationLevel;
import com.gabaritai.edital.exception.ConcursoDuplicadoException;
import com.gabaritai.edital.exception.ConcursoNaoEncontradoException;
import com.gabaritai.edital.repository.ConcursoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC-006 — Cadastrar concurso; UC-007 — Visualizar concursos cadastrados (RF03). */
@Service
public class ConcursoService {

    private final ConcursoRepository concursoRepository;

    public ConcursoService(ConcursoRepository concursoRepository) {
        this.concursoRepository = concursoRepository;
    }

    @Transactional
    public Concurso criarConcurso(
            UUID userId,
            String orgao,
            String bancaOrganizadora,
            String cargo,
            String numeroEdital,
            LocalDate dataProva,
            EducationLevel escolaridade,
            List<String> disciplinas,
            Integer quantidadeVagas,
            String linkOficial,
            ConcursoStatus status) {
        if (concursoRepository.existsByUserIdAndOrgaoIgnoreCaseAndCargoIgnoreCaseAndBancaOrganizadoraIgnoreCase(
                userId, orgao, cargo, bancaOrganizadora)) {
            throw new ConcursoDuplicadoException();
        }

        Concurso concurso = new Concurso(
                UUID.randomUUID(),
                userId,
                orgao,
                bancaOrganizadora,
                cargo,
                numeroEdital,
                dataProva,
                escolaridade,
                disciplinas,
                quantidadeVagas,
                linkOficial,
                status);
        return concursoRepository.save(concurso);
    }

    @Transactional(readOnly = true)
    public List<Concurso> listarConcursos(UUID userId, ConcursoStatus statusFiltro) {
        return statusFiltro == null
                ? concursoRepository.findByUserIdOrderByCreatedAtDesc(userId)
                : concursoRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, statusFiltro);
    }

    @Transactional(readOnly = true)
    public Concurso buscarConcurso(UUID userId, UUID concursoId) {
        return concursoRepository.findByIdAndUserId(concursoId, userId)
                .orElseThrow(ConcursoNaoEncontradoException::new);
    }
}
