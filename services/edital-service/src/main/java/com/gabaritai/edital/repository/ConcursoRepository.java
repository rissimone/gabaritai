package com.gabaritai.edital.repository;

import com.gabaritai.edital.domain.Concurso;
import com.gabaritai.edital.domain.ConcursoStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcursoRepository extends JpaRepository<Concurso, UUID> {

    List<Concurso> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<Concurso> findByUserIdAndStatusOrderByCreatedAtDesc(UUID userId, ConcursoStatus status);

    Optional<Concurso> findByIdAndUserId(UUID id, UUID userId);

    boolean existsByUserIdAndOrgaoIgnoreCaseAndCargoIgnoreCaseAndBancaOrganizadoraIgnoreCase(
            UUID userId, String orgao, String cargo, String bancaOrganizadora);
}
