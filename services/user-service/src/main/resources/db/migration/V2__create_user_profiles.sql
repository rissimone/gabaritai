-- RF02 / UC-004: perfil e preferencias do candidato.
--
-- user_id NAO e uma foreign key para outro servico (RNF-006: cada servico tem seu proprio banco).
-- E apenas o mesmo UUID gerado pelo auth-service ao cadastrar o usuario, repassado pelo
-- API Gateway via header X-User-Id apos validar o JWT.

CREATE TABLE user_profiles (
    user_id                 UUID PRIMARY KEY,
    photo_url               VARCHAR(500),
    phone                   VARCHAR(20),
    timezone                VARCHAR(50) NOT NULL DEFAULT 'America/Sao_Paulo',
    receive_notifications   BOOLEAN NOT NULL DEFAULT true,
    experience_level        VARCHAR(30),
    study_goal              VARCHAR(500),
    created_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    version                 BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT chk_user_profiles_experience_level
        CHECK (experience_level IS NULL OR experience_level IN ('SEM_EXPERIENCIA', 'INICIANTE', 'INTERMEDIARIO', 'EXPERIENTE'))
);

CREATE TABLE user_profile_learning_preferences (
    user_id     UUID NOT NULL REFERENCES user_profiles (user_id) ON DELETE CASCADE,
    preference  VARCHAR(30) NOT NULL,
    PRIMARY KEY (user_id, preference),
    CONSTRAINT chk_learning_preferences_value
        CHECK (preference IN ('TEORIA', 'QUESTOES', 'REVISAO', 'VIDEO'))
);
