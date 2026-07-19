package com.gabaritai.edital.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Concurso cadastrado por um candidato (RF03, UC-006/UC-007). {@code userId} e o mesmo
 * identificador gerado pelo auth-service — sem chave estrangeira entre bancos (RNF-006).
 */
@Entity
@Table(name = "concursos")
public class Concurso {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(nullable = false, length = 200)
    private String orgao;

    @Column(name = "banca_organizadora", nullable = false, length = 200)
    private String bancaOrganizadora;

    @Column(nullable = false, length = 200)
    private String cargo;

    @Column(name = "numero_edital", length = 50)
    private String numeroEdital;

    @Column(name = "data_prova")
    private LocalDate dataProva;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EducationLevel escolaridade;

    // EAGER: colecao pequena sempre necessaria ao serializar a resposta, fora da transacao
    // do service (mesmo ajuste feito em UserProfile.learningPreferences, ver user-service).
    // Set (nao List): a chave primaria da tabela e (concurso_id, disciplina), entao nao ha
    // coluna de ordem — um @OrderColumn apontando para a mesma coluna do elemento quebra o
    // mapeamento do Hibernate (duas propriedades para a mesma coluna).
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "concurso_disciplinas", joinColumns = @JoinColumn(name = "concurso_id"))
    @Column(name = "disciplina")
    private Set<String> disciplinas = new LinkedHashSet<>();

    @Column(name = "quantidade_vagas")
    private Integer quantidadeVagas;

    @Column(name = "link_oficial", length = 500)
    private String linkOficial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ConcursoStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(nullable = false)
    private long version;

    protected Concurso() {
        // JPA
    }

    public Concurso(
            UUID id,
            UUID userId,
            String orgao,
            String bancaOrganizadora,
            String cargo,
            String numeroEdital,
            LocalDate dataProva,
            EducationLevel escolaridade,
            Collection<String> disciplinas,
            Integer quantidadeVagas,
            String linkOficial,
            ConcursoStatus status) {
        this.id = id;
        this.userId = userId;
        this.orgao = orgao;
        this.bancaOrganizadora = bancaOrganizadora;
        this.cargo = cargo;
        this.numeroEdital = numeroEdital;
        this.dataProva = dataProva;
        this.escolaridade = escolaridade;
        this.disciplinas = disciplinas != null ? new LinkedHashSet<>(disciplinas) : new LinkedHashSet<>();
        this.quantidadeVagas = quantidadeVagas;
        this.linkOficial = linkOficial;
        this.status = status != null ? status : ConcursoStatus.PREVISTO;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getOrgao() {
        return orgao;
    }

    public String getBancaOrganizadora() {
        return bancaOrganizadora;
    }

    public String getCargo() {
        return cargo;
    }

    public String getNumeroEdital() {
        return numeroEdital;
    }

    public LocalDate getDataProva() {
        return dataProva;
    }

    public EducationLevel getEscolaridade() {
        return escolaridade;
    }

    public Set<String> getDisciplinas() {
        return disciplinas;
    }

    public Integer getQuantidadeVagas() {
        return quantidadeVagas;
    }

    public String getLinkOficial() {
        return linkOficial;
    }

    public ConcursoStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public long getVersion() {
        return version;
    }
}
