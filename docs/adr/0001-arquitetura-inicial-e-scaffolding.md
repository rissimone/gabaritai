# ADR-0001 — Arquitetura inicial, monorepo e scaffolding do GabaritAI

**Status:** aceita
**Data:** 2026-07-15
**Requisitos relacionados:** RNF-001 a RNF-011, RNF-015 a RNF-025, RNF-070, RNF-078 a RNF-098, RNF-125 a RNF-129

## Contexto

O GabaritAI está começando do zero. É necessário definir a estrutura inicial do repositório, a divisão de microsserviços, a estratégia de banco de dados por serviço e a organização de containers/redes antes de implementar qualquer regra de negócio, conforme RNF-006 a RNF-011 e RNF-070.

## Decisão

### Divisão de microsserviços

Seguindo a recomendação da seção 19 de `docs/requisitos-nao-funcionais.md` ("não criar muitos microsserviços de imediato"), o MVP terá 6 serviços de negócio, cada um mapeado a um bloco de requisitos funcionais:

| Serviço | Requisitos principais |
|---|---|
| `auth-service` | RF01 (cadastro, login, recuperação de senha, confirmação de e-mail) |
| `user-service` | RF02 (perfil e preferências do candidato) |
| `edital-service` | RF03–RF06 (concursos, importação e revisão de editais) |
| `study-plan-service` | RF07–RF20 (geração, personalização, replanejamento, revisões) |
| `ai-service` | RF05, RF23, RF26–RF29 (extração por IA, tutor, geração de conteúdo) |
| `notification-service` | RF36, RF55–RF58 (notificações e lembretes) |

Serviços de questões/simulados (RF21-RF25), materiais (RF30-RF33), pagamentos (RF39-RF41) e administração (RF42-RF45) serão adicionados em iterações futuras, quando o MVP inicial (seção 14 de `requisitos-funcionais.md`) estiver implementado — evita over-engineering neste momento (regra "Restrições" do agente).

### Monorepo

Adotado o monorepo sugerido em RNF-070, organizado por diretório:

```
GabaritAI/
├── frontend/
├── api-gateway/
├── services/
│   ├── auth-service/
│   ├── user-service/
│   ├── edital-service/
│   ├── study-plan-service/
│   ├── ai-service/
│   └── notification-service/
├── infrastructure/
│   ├── docker/
│   ├── jenkins/
│   ├── monitoring/
│   └── database/
├── docs/
└── scripts/
```

Cada serviço mantém seu próprio `pom.xml`/`package.json`, `Dockerfile` e `Jenkinsfile`, podendo ser buildado, testado e implantado de forma independente (RNF-007), apesar de residirem no mesmo repositório Git.

### Banco de dados por serviço

RNF-003 permite "banco de dados **ou** schema isolado". Optou-se por uma instância PostgreSQL dedicada por microsserviço (não um schema compartilhado numa instância única), pelos seguintes motivos:

* elimina qualquer possibilidade de um serviço acessar tabelas de outro (RNF-006), sem depender de convenção de schema;
* permite escalar/parar/fazer backup de cada banco de forma independente (RNF-007, RNF-053);
* é o cenário mais próximo do ambiente de produção, reduzindo divergência entre dev e produção (RNF-091).

O custo é mais containers em desenvolvimento local; considerado aceitável para a fase atual do projeto.

### Comunicação assíncrona

RabbitMQ foi escolhido inicialmente para o MVP, conforme sugestão original de RNF-108 ("RabbitMQ para uma operação inicial mais simples"), compartilhado entre os serviços via `gabaritai-application` — a mensageria em si não guarda dados de negócio, então uma instância compartilhada não viola o isolamento de dados do RNF-003.

> **Atualização (2026-07-16):** `requisitos-nao-funcionais.md` passou a exigir Apache Kafka em vez de RabbitMQ, em função de uma perspectiva de aumento no volume de mensageria do projeto. Ver [ADR-0003](0003-troca-rabbitmq-por-kafka.md) para a decisão e o impacto. O restante desta seção (rede compartilhada, ausência de dados de negócio no broker) continua válido — apenas o produto trocou.

### Redes de containers

Implementadas as 5 redes mínimas exigidas por RNF-017:

* `gabaritai-public`: reverse proxy, frontend, api-gateway (únicos com porta publicada — RNF-018);
* `gabaritai-application`: api-gateway, microsserviços, Kafka (ver ADR-0003);
* `gabaritai-data`: microsserviços e seus respectivos bancos PostgreSQL;
* `gabaritai-monitoring`: reservada para Prometheus/Grafana/Loki (a implementar em iteração futura de observabilidade);
* `gabaritai-cicd`: reservada para Jenkins e agentes (infraestrutura de CI/CD será tratada em ADR futuro, quando o Jenkins for provisionado).

## Alternativas consideradas

* **Schema único compartilhado por serviço em uma instância Postgres única** — mais leve para dev local, mas exige disciplina de convenção para não vazar acesso entre schemas e diverge mais do ambiente de produção recomendado. Rejeitada em favor do isolamento mais forte.
* **Kafka desde o início** — maior complexidade operacional sem justificativa de volume/streaming no MVP (RNF-108 recomenda avaliar Kafka apenas se o volume justificar). Adiado.
* **Múltiplos repositórios (um por serviço)** — mais alinhado à independência total, mas aumenta o custo de coordenação para uma equipe pequena em fase inicial; RNF-070 já recomenda monorepo neste cenário.

## Consequências

* Sobe de 1 para 6 containers de banco de dados no `docker-compose.yml` local — maior consumo de memória/CPU em desenvolvimento, mitigado por limites de recursos (RNF-055) a serem ajustados conforme necessário.
* Qualquer novo microsserviço decidido no futuro deve repetir o mesmo padrão (Dockerfile multi-stage, Jenkinsfile, banco dedicado, redes `application`+`data`).
* Este ADR não cobre observabilidade (Prometheus/Grafana/Loki) nem o pipeline Jenkins completo em execução — apenas a estrutura e placeholders; serão detalhados em ADRs futuros quando efetivamente implementados.
