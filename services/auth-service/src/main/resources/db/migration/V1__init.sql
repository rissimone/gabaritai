-- Baseline migration for auth-service.
-- Domain tables are added incrementally as features from docs/requisitos-funcionais.md are implemented.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
