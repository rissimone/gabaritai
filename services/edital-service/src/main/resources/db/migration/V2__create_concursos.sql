-- RF03 / UC-006, UC-007: cadastro e consulta de concursos.

CREATE TABLE concursos (
    id                     UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id                UUID NOT NULL,
    orgao                  VARCHAR(200) NOT NULL,
    banca_organizadora     VARCHAR(200) NOT NULL,
    cargo                  VARCHAR(200) NOT NULL,
    numero_edital          VARCHAR(50),
    data_prova             DATE,
    escolaridade           VARCHAR(20),
    quantidade_vagas       INTEGER,
    link_oficial           VARCHAR(500),
    status                 VARCHAR(30) NOT NULL DEFAULT 'PREVISTO',
    created_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
    version                BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT chk_concursos_escolaridade
        CHECK (escolaridade IS NULL OR escolaridade IN ('FUNDAMENTAL', 'MEDIO', 'TECNICO', 'SUPERIOR')),
    CONSTRAINT chk_concursos_status
        CHECK (status IN ('PREVISTO', 'EDITAL_PUBLICADO', 'INSCRICOES_ABERTAS', 'PROVA_REALIZADA')),
    CONSTRAINT chk_concursos_quantidade_vagas
        CHECK (quantidade_vagas IS NULL OR quantidade_vagas > 0)
);

-- Consulta mais comum: concursos de um usuario, filtrados por status (UC-007).
CREATE INDEX idx_concursos_user_id ON concursos (user_id);
CREATE INDEX idx_concursos_user_id_status ON concursos (user_id, status);

CREATE TABLE concurso_disciplinas (
    concurso_id  UUID NOT NULL REFERENCES concursos (id) ON DELETE CASCADE,
    disciplina   VARCHAR(150) NOT NULL,
    PRIMARY KEY (concurso_id, disciplina)
);
