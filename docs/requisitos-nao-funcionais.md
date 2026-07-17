# Requisitos Não Funcionais — GabaritAI

## 1. Tecnologias e padronização

### RNF-001 — Tecnologia do backend

O backend da aplicação deve ser desenvolvido utilizando Java com Spring Boot.

A versão do Java deve ser uma versão LTS com suporte ativo, preferencialmente Java 21 ou superior.

Os microsserviços devem utilizar, sempre que aplicável:

* Spring Boot;
* Spring Web;
* Spring Data JPA;
* Spring Security;
* Spring Validation;
* Spring Actuator;
* Spring Cloud;
* Maven ou Gradle para gerenciamento de dependências.

### RNF-002 — Tecnologia do frontend

O frontend deve ser desenvolvido utilizando Angular e TypeScript.

A aplicação deve utilizar uma versão estável e com suporte ativo do Angular.

O projeto frontend deve seguir uma arquitetura modular, separando:

* componentes;
* páginas;
* serviços;
* modelos;
* interceptadores;
* guardas de rota;
* módulos ou funcionalidades;
* componentes compartilhados;
* gerenciamento de estado, quando necessário.

### RNF-003 — Banco de dados

O banco de dados principal deve ser o PostgreSQL.

O acesso ao banco deve ser realizado por meio de mecanismos seguros de conexão, utilizando pool de conexões.

Cada microsserviço deve possuir seu próprio banco de dados ou schema isolado, evitando o compartilhamento direto de tabelas entre serviços.

### RNF-004 — Padronização de código

Os projetos devem possuir padrões de desenvolvimento documentados.

No backend, devem ser adotadas convenções para:

* nomes de classes;
* organização de pacotes;
* tratamento de exceções;
* objetos de entrada e saída;
* logs;
* validações;
* documentação de APIs;
* testes automatizados.

No frontend, devem ser definidos padrões para:

* organização de componentes;
* nomes de arquivos;
* uso de interfaces;
* serviços;
* observables;
* gerenciamento de estado;
* tratamento de erros;
* formulários;
* estilização.

### RNF-005 — Idioma técnico

O código-fonte, nomes de classes, métodos, variáveis, tabelas, endpoints e mensagens de log devem ser escritos preferencialmente em inglês.

Os textos apresentados ao usuário devem ser disponibilizados em português brasileiro, com suporte futuro à internacionalização.

---

## 2. Arquitetura de microsserviços

### RNF-006 — Arquitetura distribuída

A aplicação deve utilizar uma arquitetura baseada em microsserviços.

Cada microsserviço deve possuir responsabilidade de negócio bem definida e baixo acoplamento com os demais serviços.

Possíveis microsserviços iniciais:

* serviço de autenticação;
* serviço de usuários;
* serviço de concursos e editais;
* serviço de processamento de documentos;
* serviço de inteligência artificial;
* serviço de planos de estudo;
* serviço de acompanhamento de progresso;
* serviço de notificações;
* serviço de pagamentos e assinaturas;
* serviço administrativo.

### RNF-007 — Independência dos serviços

Cada microsserviço deve poder ser:

* desenvolvido independentemente;
* testado independentemente;
* versionado independentemente;
* implantado independentemente;
* escalado independentemente;
* reiniciado sem comprometer toda a plataforma.

### RNF-008 — Comunicação entre microsserviços

A comunicação síncrona entre os serviços deve utilizar APIs REST sobre HTTPS.

Operações assíncronas, demoradas ou que não exijam resposta imediata devem utilizar mensageria.

Exemplos:

* processamento de edital;
* geração de plano por IA;
* envio de notificações;
* atualização de métricas;
* processamento de pagamentos;
* geração de relatórios.

### RNF-009 — API Gateway

As requisições externas devem ser centralizadas por meio de um API Gateway.

O API Gateway deve ser responsável por:

* roteamento;
* autenticação;
* autorização inicial;
* limitação de requisições;
* correlação de requisições;
* padronização de cabeçalhos;
* tratamento de CORS;
* observabilidade;
* proteção dos serviços internos.

### RNF-010 — Descoberta de serviços

A solução deve disponibilizar um mecanismo de descoberta e localização dos microsserviços.

Em ambientes baseados em Docker Compose, os nomes dos serviços poderão ser utilizados como DNS interno.

Em ambientes orquestrados, deve ser utilizado o mecanismo nativo de descoberta da plataforma.

### RNF-011 — Configuração centralizada

As configurações dos microsserviços devem ser externalizadas.

Informações como URLs, portas, credenciais, limites e parâmetros de execução não devem ficar fixas no código-fonte.

As configurações devem ser fornecidas por:

* variáveis de ambiente;
* arquivos específicos por ambiente;
* serviço centralizado de configuração;
* mecanismo seguro de gerenciamento de segredos.

### RNF-012 — Resiliência

Os microsserviços devem implementar mecanismos de resiliência, quando aplicável:

* timeout;
* retry controlado;
* circuit breaker;
* fallback;
* bulkhead;
* limitação de concorrência;
* filas de retentativa;
* filas de mensagens não processadas.

### RNF-013 — Idempotência

Operações críticas devem ser idempotentes sempre que possível.

Uma mesma requisição repetida não deve gerar duplicidade em processos como:

* geração de assinatura;
* cobrança;
* criação de plano;
* processamento de edital;
* envio de notificações;
* confirmação de atividades.

### RNF-014 — Consistência distribuída

Processos que envolvam múltiplos microsserviços não devem utilizar transações distribuídas fortemente acopladas.

Deve-se priorizar:

* consistência eventual;
* eventos de domínio;
* padrão Saga;
* compensação de transações;
* Outbox Pattern.

---

## 3. Containers e redes

### RNF-015 — Containerização

Todos os microsserviços devem ser executáveis em containers.

Cada serviço deve possuir seu próprio Dockerfile.

Também devem ser containerizados, quando aplicável:

* frontend;
* API Gateway;
* PostgreSQL;
* mensageria;
* Jenkins;
* ferramentas de monitoramento;
* ferramentas de logs;
* proxy reverso.

### RNF-016 — Imagens de container

As imagens devem:

* utilizar imagens-base oficiais;
* utilizar versões explicitamente definidas;
* evitar a tag `latest`;
* possuir tamanho reduzido;
* executar com usuário não root;
* conter apenas dependências necessárias;
* ser construídas preferencialmente com múltiplos estágios;
* passar por verificação de vulnerabilidades.

### RNF-017 — Redes de containers

A infraestrutura deve ser organizada por redes de containers distintas.

Devem existir, no mínimo:

* rede pública;
* rede de aplicação;
* rede de dados;
* rede de monitoramento;
* rede de integração contínua.

### RNF-018 — Rede pública

A rede pública deve conter apenas os componentes que precisam receber conexões externas.

Exemplos:

* proxy reverso;
* frontend;
* API Gateway.

Microsserviços internos e bancos de dados não devem ser expostos diretamente à internet.

### RNF-019 — Rede de aplicação

A rede de aplicação deve permitir a comunicação entre:

* API Gateway;
* microsserviços;
* serviços de autenticação;
* mensageria;
* componentes internos.

### RNF-020 — Rede de dados

A rede de dados deve ser isolada e utilizada para comunicação com:

* PostgreSQL;
* cache;
* mecanismos de busca;
* armazenamento interno;
* serviços de persistência.

Somente serviços autorizados devem possuir acesso à rede de dados.

### RNF-021 — Rede de monitoramento

A rede de monitoramento deve conter os serviços responsáveis por:

* coleta de métricas;
* centralização de logs;
* rastreamento distribuído;
* dashboards;
* alertas.

### RNF-022 — Rede de CI/CD

A rede de integração contínua deve conter componentes como:

* Jenkins;
* agentes Jenkins;
* registry de imagens;
* ferramentas de análise de código;
* ferramentas de segurança.

O acesso dessa rede aos ambientes de execução deve ser limitado ao necessário para implantação.

### RNF-023 — Persistência em containers

Dados persistentes não devem ser armazenados apenas no sistema de arquivos interno dos containers.

Devem ser utilizados volumes para:

* PostgreSQL;
* Jenkins;
* registry;
* ferramentas de monitoramento;
* logs persistentes;
* arquivos importados;
* backups.

### RNF-024 — Health checks

Todos os containers devem possuir mecanismos de verificação de saúde.

Os microsserviços Spring Boot devem disponibilizar endpoints por meio do Spring Boot Actuator.

Devem ser verificadas condições como:

* disponibilidade da aplicação;
* conexão com banco;
* conexão com mensageria;
* disponibilidade de serviços dependentes;
* espaço de armazenamento.

### RNF-025 — Inicialização e dependências

A inicialização dos containers deve respeitar a disponibilidade real das dependências.

Não deve ser considerado suficiente apenas verificar se o container foi iniciado.

O serviço dependente deve aguardar o health check da dependência.

---

## 4. Segurança

### RNF-026 — Autenticação

A autenticação deve utilizar um padrão seguro baseado em tokens.

Podem ser utilizados:

* OAuth 2.0;
* OpenID Connect;
* JWT;
* servidor de identidade dedicado.

### RNF-027 — Autorização

O sistema deve implementar controle de acesso baseado em perfis e permissões.

Perfis iniciais:

* candidato;
* administrador;
* suporte;
* produtor de conteúdo;
* auditor.

### RNF-028 — Proteção de senhas

Senhas nunca devem ser armazenadas em texto puro.

Devem ser utilizadas funções seguras de hash, como:

* Argon2;
* bcrypt.

### RNF-029 — Comunicação segura

Toda comunicação externa deve utilizar HTTPS.

Comunicações internas que trafeguem informações sensíveis também devem utilizar criptografia.

### RNF-030 — Segredos

Credenciais, tokens, chaves de API, senhas e certificados não devem ser armazenados no repositório Git.

Esses valores devem ser gerenciados por:

* variáveis protegidas;
* Jenkins Credentials;
* Docker Secrets;
* Vault;
* gerenciadores de segredos de provedores de nuvem.

### RNF-031 — Dados sensíveis

Dados sensíveis devem ser protegidos durante:

* armazenamento;
* processamento;
* transmissão;
* backup;
* registro em logs.

### RNF-032 — Validação de entrada

Todos os dados recebidos devem ser validados no frontend e obrigatoriamente no backend.

Devem ser prevenidos ataques como:

* SQL Injection;
* Cross-Site Scripting;
* Cross-Site Request Forgery;
* Command Injection;
* upload de arquivos maliciosos;
* acesso indevido a recursos;
* manipulação de identificadores.

### RNF-033 — Upload seguro

Arquivos de editais e materiais enviados pelos usuários devem ser validados quanto a:

* extensão;
* MIME type;
* tamanho;
* conteúdo;
* presença de malware;
* arquivos corrompidos;
* arquivos protegidos por senha.

### RNF-034 — Rate limiting

O sistema deve limitar requisições por usuário, endereço IP, plano e endpoint.

A limitação deve ser mais restritiva em recursos de alto custo, como:

* processamento de PDF;
* interações com IA;
* geração de simulados;
* geração de plano;
* envio de e-mails.

### RNF-035 — Auditoria de segurança

O sistema deve registrar eventos relevantes, como:

* login;
* falha de autenticação;
* alteração de senha;
* mudança de permissão;
* acesso administrativo;
* alteração de assinatura;
* processamento de pagamento;
* exclusão de dados.

### RNF-036 — Atualização de dependências

As dependências devem ser verificadas regularmente quanto a vulnerabilidades.

O pipeline deve executar ferramentas para identificar:

* bibliotecas vulneráveis;
* imagens de container vulneráveis;
* dependências desatualizadas;
* segredos acidentalmente versionados.

---

## 5. LGPD e privacidade

### RNF-037 — Conformidade com a LGPD

O sistema deve respeitar os princípios e obrigações aplicáveis da Lei Geral de Proteção de Dados.

### RNF-038 — Consentimento

O sistema deve registrar o consentimento do usuário para:

* tratamento de dados;
* comunicações;
* uso de cookies não essenciais;
* processamento de documentos;
* uso de dados para personalização.

### RNF-039 — Exclusão de dados

O usuário deve poder solicitar a exclusão de sua conta e de seus dados pessoais.

A exclusão deve considerar:

* dados cadastrais;
* planos de estudo;
* histórico;
* documentos importados;
* interações com IA;
* materiais pessoais.

Dados que precisem ser mantidos por obrigação legal devem ser bloqueados e protegidos.

### RNF-040 — Exportação de dados

O usuário deve poder solicitar uma cópia dos dados associados à sua conta em formato legível e estruturado.

### RNF-041 — Minimização de dados

O sistema deve coletar apenas os dados necessários para a execução das funcionalidades.

### RNF-042 — Retenção

Devem ser definidos prazos de retenção para:

* documentos;
* logs;
* dados de usuários;
* backups;
* interações com IA;
* informações de cobrança;
* registros de auditoria.

### RNF-043 — Anonimização

Dados utilizados para análises estatísticas e melhoria da plataforma devem ser anonimizados ou pseudonimizados sempre que possível.

---

## 6. Desempenho

### RNF-044 — Tempo de resposta

As operações comuns da API devem possuir tempo de resposta de até dois segundos em condições normais.

Exemplos:

* autenticação;
* consulta de perfil;
* consulta do plano;
* atualização de atividade;
* consulta de progresso.

### RNF-045 — Carregamento do frontend

As principais páginas do frontend devem ser carregadas em até três segundos em uma conexão de internet estável.

### RNF-046 — Processamentos demorados

Operações de longa duração devem ser executadas de forma assíncrona.

Exemplos:

* leitura de edital;
* OCR;
* extração de conteúdo;
* análise com IA;
* geração de plano;
* processamento de retificações;
* geração de relatórios.

### RNF-047 — Paginação

Consultas que possam retornar grandes volumes de dados devem possuir paginação.

### RNF-048 — Cache

Informações acessadas frequentemente e com baixa frequência de alteração devem utilizar cache.

Possíveis dados:

* concursos;
* disciplinas;
* configurações;
* planos ativos;
* editais públicos;
* permissões;
* resultados de processamento reutilizáveis.

### RNF-049 — Pool de conexões

Cada microsserviço deve utilizar pool de conexões configurado conforme sua carga.

O número de conexões deve ser limitado para evitar esgotamento do PostgreSQL.

### RNF-050 — Processamento de arquivos

O upload e processamento de arquivos não deve bloquear threads responsáveis por requisições comuns da aplicação.

---

## 7. Escalabilidade

### RNF-051 — Escalabilidade horizontal

Os microsserviços devem ser preparados para execução em múltiplas instâncias.

### RNF-052 — Serviços stateless

Sempre que possível, os microsserviços devem ser stateless.

Dados de sessão não devem depender da memória local de uma única instância.

### RNF-053 — Escalabilidade por serviço

Serviços com maior consumo devem poder ser escalados separadamente.

Exemplos:

* processamento de editais;
* IA;
* notificações;
* geração de relatórios;
* banco de questões.

### RNF-054 — Filas de processamento

As filas devem permitir controlar picos de processamento e impedir sobrecarga dos serviços.

### RNF-055 — Limites de recursos

Cada container deve possuir limites e reservas configuráveis de:

* CPU;
* memória;
* armazenamento;
* processos;
* conexões.

---

## 8. Disponibilidade e recuperação

### RNF-056 — Disponibilidade

A aplicação deve possuir disponibilidade mensal mínima de 99,5% durante a fase inicial.

Esse valor poderá ser aumentado conforme a maturidade do produto.

### RNF-057 — Ausência de ponto único de falha

Componentes críticos devem possuir estratégia de redundância em produção.

### RNF-058 — Backup

O PostgreSQL deve possuir rotina automática de backup.

Os backups devem incluir:

* backup completo;
* backups incrementais ou logs de transação;
* política de retenção;
* armazenamento separado do ambiente principal.

### RNF-059 — Teste de restauração

A restauração dos backups deve ser testada periodicamente.

Um backup não deve ser considerado válido apenas por ter sido criado.

### RNF-060 — Objetivos de recuperação

Devem ser definidos:

* RPO, que representa a perda máxima aceitável de dados;
* RTO, que representa o tempo máximo aceitável para recuperação.

Como meta inicial:

* RPO de até 24 horas;
* RTO de até 4 horas.

Para dados críticos de assinatura e pagamento, devem ser adotados valores mais restritivos.

### RNF-061 — Graceful shutdown

Os microsserviços devem finalizar conexões e tarefas de forma controlada durante reinicializações e implantações.

---

## 9. Observabilidade

### RNF-062 — Logs centralizados

Os logs dos microsserviços devem ser centralizados.

As mensagens devem conter:

* data e hora;
* nível;
* serviço;
* ambiente;
* identificador da requisição;
* identificador de correlação;
* classe de origem;
* mensagem;
* código do erro.

### RNF-063 — Logs estruturados

Os logs devem ser produzidos preferencialmente em formato estruturado, como JSON.

### RNF-064 — Proteção dos logs

Logs não devem armazenar:

* senhas;
* tokens;
* dados completos de cartão;
* chaves de API;
* documentos completos;
* dados pessoais desnecessários.

### RNF-065 — Métricas

Os serviços devem expor métricas por meio do Spring Boot Actuator e, preferencialmente, Micrometer.

Devem ser monitoradas:

* quantidade de requisições;
* tempo de resposta;
* taxa de erros;
* uso de CPU;
* uso de memória;
* número de conexões;
* filas;
* consumo da IA;
* falhas de processamento;
* disponibilidade.

### RNF-066 — Rastreamento distribuído

A aplicação deve implementar rastreamento distribuído.

Cada requisição deve possuir um identificador de correlação propagado entre os microsserviços.

### RNF-067 — Dashboards

Devem existir dashboards para acompanhamento de:

* saúde dos serviços;
* desempenho;
* erros;
* infraestrutura;
* filas;
* banco de dados;
* consumo de IA;
* uso do sistema.

### RNF-068 — Alertas

Alertas devem ser configurados para eventos como:

* serviço indisponível;
* aumento da taxa de erros;
* alto consumo de CPU;
* alto consumo de memória;
* espaço em disco reduzido;
* falha de backup;
* fila acumulada;
* lentidão no banco;
* excesso de chamadas à IA;
* falha no pipeline.

---

## 10. Versionamento com GitHub

### RNF-069 — Repositórios Git

O código-fonte deve ser versionado utilizando Git e hospedado no GitHub.

### RNF-070 — Organização dos repositórios

A equipe deve definir entre:

* monorepositório;
* repositório por microsserviço.

Para uma equipe pequena e um produto em fase inicial, pode-se utilizar um monorepositório organizado por diretórios.

Exemplo:

```text
gabaritai/
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

### RNF-071 — Estratégia de branches

O projeto deve possuir uma estratégia de branches documentada.

Sugestão:

* `main`: código de produção;
* `develop`: integração para o próximo ciclo;
* `feature/*`: novas funcionalidades;
* `bugfix/*`: correções;
* `hotfix/*`: correções urgentes de produção;
* `release/*`: preparação de versões.

Para uma equipe menor, também pode ser adotado trunk-based development.

### RNF-072 — Pull requests

Alterações nas branches protegidas devem ocorrer exclusivamente por pull requests.

### RNF-073 — Revisão de código

Pull requests devem possuir pelo menos uma aprovação antes do merge.

### RNF-074 — Proteção de branches

As branches `main` e `develop` devem possuir regras de proteção.

Devem ser exigidos:

* pipeline aprovado;
* testes aprovados;
* análise de código aprovada;
* ausência de conflitos;
* revisão de código;
* histórico atualizado.

### RNF-075 — Commits

Os commits devem possuir mensagens padronizadas.

Pode ser adotado o padrão Conventional Commits.

Exemplos:

```text
feat: add study plan generation
fix: correct edital topic extraction
refactor: simplify authentication flow
test: add unit tests for edital service
docs: update deployment instructions
```

### RNF-076 — Versionamento semântico

As versões da aplicação devem seguir versionamento semântico.

Formato:

```text
MAJOR.MINOR.PATCH
```

### RNF-077 — Tags e releases

Toda versão implantada em produção deve possuir:

* tag;
* release no GitHub;
* changelog;
* referência ao pipeline;
* identificação das imagens implantadas.

---

## 11. Integração contínua com Jenkins

### RNF-078 — Jenkins

O Jenkins deve ser utilizado como principal ferramenta de integração e entrega contínuas.

### RNF-079 — Pipeline como código

Os pipelines devem ser definidos em arquivos versionados no repositório.

Deve ser utilizado um `Jenkinsfile`.

### RNF-080 — Pipeline do backend

O pipeline de cada microsserviço backend deve executar, no mínimo:

1. checkout do código;
2. validação de padrões;
3. compilação;
4. testes unitários;
5. testes de integração;
6. análise estática;
7. análise de vulnerabilidades;
8. empacotamento da aplicação;
9. construção da imagem Docker;
10. escaneamento da imagem;
11. publicação da imagem;
12. implantação no ambiente;
13. testes de saúde;
14. notificação do resultado.

### RNF-081 — Pipeline do frontend

O pipeline do Angular deve executar:

1. instalação de dependências;
2. verificação de formatação;
3. lint;
4. testes unitários;
5. build de produção;
6. análise de vulnerabilidades;
7. construção da imagem Docker;
8. publicação da imagem;
9. implantação;
10. smoke test.

### RNF-082 — Disparo automático

O Jenkins deve iniciar pipelines automaticamente a partir de eventos do GitHub.

Eventos possíveis:

* abertura de pull request;
* atualização de pull request;
* merge em `develop`;
* merge em `main`;
* criação de tag;
* execução manual autorizada.

### RNF-083 — Qualidade obrigatória

O pipeline deve ser interrompido quando ocorrer:

* falha de compilação;
* falha de testes;
* cobertura abaixo do limite;
* vulnerabilidade crítica;
* análise de qualidade reprovada;
* falha na construção da imagem;
* falha na implantação;
* health check reprovado.

### RNF-084 — Artefatos versionados

Os artefatos gerados devem possuir identificação única.

Exemplo:

```text
gabaritai/user-service:1.4.2
gabaritai/user-service:commit-a1b2c3d
```

### RNF-085 — Registry de imagens

As imagens Docker devem ser publicadas em um registry privado ou controlado.

Possibilidades:

* GitHub Container Registry;
* Docker Hub privado;
* Harbor;
* registry de provedor de nuvem.

### RNF-086 — Credenciais no Jenkins

O Jenkins deve utilizar seu mecanismo de gerenciamento de credenciais.

Credenciais não devem ser incluídas diretamente no `Jenkinsfile`.

### RNF-087 — Agentes Jenkins

As execuções devem utilizar agentes isolados.

Quando possível, cada build deve ocorrer em um agente temporário e descartável.

### RNF-088 — Cache do pipeline

O pipeline pode utilizar cache de dependências, desde que não comprometa a reprodutibilidade do build.

Podem ser armazenados em cache:

* dependências Maven;
* dependências npm;
* camadas Docker.

### RNF-089 — Notificações do pipeline

O Jenkins deve notificar o resultado do pipeline por um ou mais canais:

* e-mail;
* Slack;
* Microsoft Teams;
* GitHub Checks.

---

## 12. Entrega contínua

### RNF-090 — Ambientes

A aplicação deve possuir ambientes separados.

No mínimo:

* desenvolvimento;
* testes;
* homologação;
* produção.

### RNF-091 — Isolamento entre ambientes

Cada ambiente deve possuir:

* configurações próprias;
* bancos separados;
* credenciais distintas;
* redes distintas;
* volumes distintos;
* serviços de terceiros configurados separadamente.

### RNF-092 — Promoção de versão

Uma versão deve ser promovida entre ambientes sem ser reconstruída.

A mesma imagem validada em homologação deve ser implantada em produção.

### RNF-093 — Aprovação para produção

A implantação em produção deve exigir aprovação manual de uma pessoa autorizada, especialmente na fase inicial do projeto.

### RNF-094 — Estratégia de implantação

A produção deve utilizar estratégia que reduza indisponibilidade.

Possibilidades:

* rolling update;
* blue-green;
* canary.

### RNF-095 — Rollback

Toda implantação deve possuir mecanismo de rollback.

O rollback deve permitir restaurar rapidamente:

* versão anterior da aplicação;
* configurações;
* imagem de container;
* migração compatível do banco.

### RNF-096 — Migração de banco

Alterações no banco devem ser versionadas.

Devem ser utilizadas ferramentas como:

* Flyway;
* Liquibase.

As migrações devem fazer parte do pipeline.

### RNF-097 — Compatibilidade de migrações

Migrações devem ser preferencialmente retrocompatíveis durante implantações progressivas.

Alterações destrutivas devem ser realizadas em etapas.

### RNF-098 — Smoke tests

Após a implantação, o pipeline deve executar testes básicos para confirmar:

* disponibilidade;
* autenticação;
* acesso ao banco;
* funcionamento do Gateway;
* comunicação entre serviços;
* carregamento do frontend.

---

## 13. Qualidade de software

### RNF-099 — Testes unitários

Regras de negócio devem possuir testes unitários automatizados.

### RNF-100 — Cobertura de testes

A cobertura mínima inicial deve ser de 70% para os serviços backend.

Módulos críticos devem buscar cobertura superior.

### RNF-101 — Testes de integração

Devem existir testes de integração para:

* APIs;
* PostgreSQL;
* mensageria;
* autenticação;
* persistência;
* comunicação entre serviços.

### RNF-102 — Testes com containers

Testes de integração devem utilizar ambientes isolados.

Pode ser utilizada a biblioteca Testcontainers para disponibilizar instâncias temporárias de PostgreSQL e outros serviços.

### RNF-103 — Testes de contrato

A comunicação entre microsserviços deve possuir testes de contrato para prevenir alterações incompatíveis.

### RNF-104 — Testes end-to-end

Os principais fluxos do usuário devem possuir testes end-to-end.

Exemplos:

* cadastro;
* login;
* envio de edital;
* revisão do conteúdo;
* geração de plano;
* conclusão de atividade;
* contratação de assinatura.

### RNF-105 — Análise estática

O código deve passar por análise estática.

Podem ser utilizadas ferramentas como:

* SonarQube;
* Checkstyle;
* SpotBugs;
* PMD;
* ESLint.

### RNF-106 — Qualidade do código

O pipeline deve avaliar:

* duplicação;
* complexidade;
* cobertura;
* vulnerabilidades;
* bugs;
* code smells;
* dívida técnica.

### RNF-107 — Testes de carga

Os principais endpoints devem ser submetidos a testes de carga antes de lançamentos relevantes.

### RNF-108 — Testes de segurança

Devem ser realizados testes periódicos de segurança, incluindo:

* análise de dependências;
* análise de containers;
* análise de APIs;
* testes de autenticação;
* testes de autorização;
* verificação de exposição de dados.

---

## 14. Banco de dados PostgreSQL

### RNF-109 — Migrações versionadas

Toda alteração de estrutura do banco deve ser versionada.

Não devem ser realizadas alterações manuais em produção sem script correspondente.

### RNF-110 — Índices

Índices devem ser criados com base nos padrões reais de consulta.

### RNF-111 — Integridade

O banco deve utilizar:

* chaves primárias;
* chaves estrangeiras, quando aplicável;
* restrições de unicidade;
* restrições de nulidade;
* validações de domínio.

### RNF-112 — Auditoria de dados

Entidades importantes devem possuir campos como:

* data de criação;
* data de atualização;
* usuário responsável;
* versão para controle de concorrência;
* status lógico.

### RNF-113 — Exclusão lógica

Dados com necessidade de histórico devem utilizar exclusão lógica quando apropriado.

### RNF-114 — Concorrência

Operações concorrentes devem ser tratadas por mecanismos de controle de versão ou bloqueio adequado.

### RNF-115 — Consultas

Devem ser evitadas:

* consultas sem limite;
* problema N+1;
* carregamentos desnecessários;
* joins excessivos;
* transações longas.

### RNF-116 — Monitoramento do PostgreSQL

Devem ser monitorados:

* conexões;
* locks;
* queries lentas;
* uso de CPU;
* uso de memória;
* espaço em disco;
* crescimento das tabelas;
* falhas de replicação, quando aplicável.

---

## 15. Frontend e experiência do usuário

### RNF-117 — Responsividade

O frontend deve ser responsivo e funcionar adequadamente em:

* desktop;
* tablet;
* celular.

### RNF-118 — Compatibilidade

A aplicação deve funcionar nas versões recentes dos principais navegadores:

* Google Chrome;
* Mozilla Firefox;
* Microsoft Edge;
* Safari.

### RNF-119 — Acessibilidade

A interface deve seguir recomendações WCAG, buscando inicialmente conformidade com o nível AA.

### RNF-120 — Feedback visual

A interface deve apresentar feedback claro durante:

* carregamento;
* upload;
* processamento de edital;
* geração de plano;
* salvamento;
* erro;
* conclusão de operação.

### RNF-121 — Processamento assíncrono no frontend

Quando uma operação estiver sendo executada em segundo plano, o usuário deve poder acompanhar seu estado.

Estados possíveis:

* aguardando;
* processando;
* concluído;
* concluído com ressalvas;
* falhou;
* cancelado.

### RNF-122 — Tratamento de erros

Mensagens técnicas não devem ser exibidas diretamente ao usuário.

A interface deve apresentar mensagens compreensíveis e disponibilizar um identificador para suporte.

### RNF-123 — Segurança no frontend

O frontend não deve armazenar informações sensíveis de forma insegura.

Deve ser evitado o armazenamento de tokens de longa duração no localStorage.

### RNF-124 — Otimização

O frontend deve utilizar, quando aplicável:

* lazy loading;
* compressão;
* minificação;
* cache;
* divisão de bundles;
* carregamento sob demanda;
* otimização de imagens.

---

## 16. Documentação

### RNF-125 — Documentação das APIs

As APIs devem ser documentadas com OpenAPI e Swagger.

### RNF-126 — Documentação da arquitetura

O projeto deve possuir documentação da arquitetura contendo:

* visão geral;
* microsserviços;
* responsabilidades;
* integrações;
* bancos;
* mensageria;
* redes;
* fluxos;
* decisões arquiteturais.

### RNF-127 — Documentação de execução

Cada projeto deve possuir instruções para:

* instalação;
* configuração;
* execução local;
* testes;
* build;
* criação de imagem;
* implantação.

### RNF-128 — Diagramas

Devem ser mantidos diagramas atualizados de:

* contexto;
* containers;
* componentes;
* implantação;
* sequência dos principais fluxos;
* infraestrutura de redes.

Pode ser utilizado o modelo C4.

### RNF-129 — Decisões arquiteturais

Decisões relevantes devem ser registradas por meio de Architecture Decision Records.

### RNF-130 — Runbooks

Devem ser documentados procedimentos para:

* indisponibilidade;
* rollback;
* restauração de backup;
* falha de banco;
* fila acumulada;
* rotação de credenciais;
* falha do Jenkins;
* falha de integração com IA.

---

## 17. Infraestrutura local sugerida

A infraestrutura local poderá utilizar Docker Compose com redes separadas.

Exemplo conceitual:

```text
Internet
   |
Reverse Proxy
   |
Frontend / API Gateway
   |
Application Network
   |
Microsserviços
   |
Data Network
   |
PostgreSQL / Redis / Mensageria
```

Redes sugeridas:

```text
gabaritai-public
gabaritai-application
gabaritai-data
gabaritai-monitoring
gabaritai-cicd
```

Somente o proxy reverso, frontend e API Gateway devem possuir portas publicadas externamente.

---

## 18. Ferramentas sugeridas

### Backend

* Java 21;
* Spring Boot;
* Spring Security;
* Spring Data JPA;
* Spring Cloud Gateway;
* Spring Boot Actuator;
* Maven;
* Flyway;
* Resilience4j;
* Testcontainers;
* JUnit;
* Mockito.

### Frontend

* Angular;
* TypeScript;
* Angular Material ou biblioteca equivalente;
* RxJS;
* ESLint;
* Jest ou Karma;
* Cypress ou Playwright.

### Banco e armazenamento

* PostgreSQL;
* Redis para cache;
* MinIO ou armazenamento compatível com S3 para arquivos.

### Mensageria

* Apache Kafka, para uma operação inicial mais simples;

### CI/CD

* GitHub;
* Jenkins;
* GitHub Webhooks;
* SonarQube;
* Nexus ou GitHub Packages;
* GitHub Container Registry ou Harbor;
* Trivy para imagens;
* OWASP Dependency-Check.

### Observabilidade

* Prometheus;
* Grafana;
* Loki ou Elasticsearch;
* OpenTelemetry;
* Tempo ou Jaeger;
* Alertmanager.

### Infraestrutura

* Docker;
* Docker Compose para desenvolvimento e ambientes iniciais;
* Kubernetes como possível evolução para produção em maior escala;
* Nginx ou Traefik como proxy reverso.

---

## 19. Critérios gerais de aceite

Os requisitos não funcionais serão considerados atendidos quando:

* todos os serviços puderem ser executados em containers;
* as redes estiverem devidamente isoladas;
* nenhum banco estiver exposto publicamente;
* todo código estiver versionado no GitHub;
* pull requests executarem pipeline automático;
* o Jenkins executar build, testes e análise de qualidade;
* imagens forem criadas e publicadas automaticamente;
* implantações forem executadas de forma controlada;
* migrações de banco forem versionadas;
* logs, métricas e traces estiverem disponíveis;
* backups forem criados e testados;
* falhas críticas bloquearem o pipeline;
* credenciais não estiverem presentes no código-fonte;
* os principais fluxos possuírem testes automatizados;
* a aplicação atender às metas definidas de desempenho e disponibilidade.

Para o início do projeto, eu recomendaria não criar muitos microsserviços de imediato. Uma primeira divisão entre autenticação, usuários, editais, planos de estudo, IA e notificações mantém a arquitetura distribuída sem tornar a operação excessivamente complexa.
