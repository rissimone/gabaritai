# GabaritAI

SaaS de apoio aos estudos para concursos públicos. O usuário importa um edital, revisa o conteúdo programático extraído por IA e recebe um plano de estudos personalizado.

A documentação de referência (fonte de verdade) está em [`docs/`](docs/):

* [`docs/requisitos-funcionais.md`](docs/requisitos-funcionais.md)
* [`docs/requisitos-nao-funcionais.md`](docs/requisitos-nao-funcionais.md)
* [`docs/casos-de-uso.md`](docs/casos-de-uso.md)
* [`docs/adr/`](docs/adr/) — Architecture Decision Records

## Arquitetura

Monorepo de microsserviços (ver [ADR-0001](docs/adr/0001-arquitetura-inicial-e-scaffolding.md)):

```
GabaritAI/
├── frontend/               Angular + TypeScript
├── api-gateway/             Spring Cloud Gateway
├── services/
│   ├── auth-service/        cadastro, login, senha (RF01)
│   ├── user-service/        perfil e preferências (RF02)
│   ├── edital-service/      concursos e editais (RF03-RF06)
│   ├── study-plan-service/  plano de estudos (RF07-RF20)
│   ├── ai-service/          extração por IA, tutor (RF05, RF23, RF26-RF29)
│   └── notification-service/notificações (RF36, RF55-RF58)
├── infrastructure/
│   ├── docker/               docker-compose e redes
│   ├── jenkins/               recursos compartilhados de CI/CD
│   ├── monitoring/            Prometheus/Grafana (futuro)
│   └── database/              scripts auxiliares de banco
└── scripts/
```

## Pré-requisitos

* Java 21 (LTS) — RNF-001
* Maven 3.9+
* Node.js 20+ e npm
* Docker e Docker Compose

## Executando localmente

Suba toda a infraestrutura (Postgres por serviço, Kafka, reverse proxy, API Gateway, frontend e microsserviços):

```bash
cd infrastructure/docker
cp .env.example .env   # ajuste credenciais locais
docker compose up --build
```

Somente o reverse proxy, o frontend e o API Gateway publicam portas no host (RNF-018); os demais serviços e bancos ficam acessíveis apenas dentro das redes internas do Compose.

Para rodar um microsserviço individualmente fora do Compose (ex.: durante desenvolvimento):

```bash
cd services/auth-service
mvn spring-boot:run
```

Para o frontend:

```bash
cd frontend
npm install
npm start
```

## Testes

Cada microsserviço:

```bash
cd services/<nome-do-servico>
mvn test
```

Frontend:

```bash
cd frontend
npm test
```

## Documentação das APIs

Cada microsserviço expõe OpenAPI/Swagger em `/swagger-ui.html` quando em execução (RNF-125).

## CI/CD

Cada serviço (backend e frontend) possui seu próprio `Jenkinsfile`, versionado junto ao código (RNF-079). O pipeline executa build, testes, análise estática, build/scan de imagem Docker, publicação no registry e implantação, conforme RNF-080/RNF-081.

## Status do projeto

Fase de scaffolding inicial — ver [ADR-0001](docs/adr/0001-arquitetura-inicial-e-scaffolding.md) para o escopo e as decisões tomadas até o momento. Nenhuma regra de negócio foi implementada ainda.
