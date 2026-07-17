-- RF01 / UC-001, UC-002, UC-003: cadastro, autenticacao e recuperacao de senha.

CREATE TABLE users (
    id                     UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name                   VARCHAR(150) NOT NULL,
    email                  VARCHAR(255) NOT NULL,
    password_hash          VARCHAR(255) NOT NULL,
    status                 VARCHAR(30) NOT NULL DEFAULT 'PENDING_CONFIRMATION',
    terms_accepted_at      TIMESTAMPTZ NOT NULL,
    failed_login_attempts  INTEGER NOT NULL DEFAULT 0,
    locked_until           TIMESTAMPTZ,
    created_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
    version                BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_status CHECK (status IN ('PENDING_CONFIRMATION', 'ACTIVE', 'BLOCKED'))
);

CREATE INDEX idx_users_email ON users (email);

CREATE TABLE email_confirmation_tokens (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id     UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token_hash  VARCHAR(255) NOT NULL,
    expires_at  TIMESTAMPTZ NOT NULL,
    used_at     TIMESTAMPTZ,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_email_confirmation_token_hash UNIQUE (token_hash)
);

CREATE INDEX idx_email_confirmation_tokens_user_id ON email_confirmation_tokens (user_id);

CREATE TABLE password_reset_tokens (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id     UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token_hash  VARCHAR(255) NOT NULL,
    expires_at  TIMESTAMPTZ NOT NULL,
    used_at     TIMESTAMPTZ,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_password_reset_token_hash UNIQUE (token_hash)
);

CREATE INDEX idx_password_reset_tokens_user_id ON password_reset_tokens (user_id);
