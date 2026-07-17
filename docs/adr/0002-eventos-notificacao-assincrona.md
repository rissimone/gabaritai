# ADR-0002 — auth-service publica eventos assincronos em vez de enviar e-mail diretamente

**Status:** aceita (o broker especifico mudou de RabbitMQ para Kafka — ver [ADR-0003](0003-troca-rabbitmq-por-kafka.md); a decisao de arquitetura event-driven descrita aqui continua valida)
**Data:** 2026-07-16
**Requisitos relacionados:** RF01, RNF-006, RNF-008, RNF-012, RNF-013, RNF-031, RNF-046

## Contexto

UC-001 (cadastro) e UC-003 (recuperacao de senha) exigem que o sistema envie um e-mail com um link de confirmacao/redefinicao. O `notification-service` (RF36, UC-056) e o responsavel documentado por envio de notificacoes, mas ainda nao foi implementado. Era preciso decidir como o `auth-service` deveria disparar esse e-mail sem violar a separacao de responsabilidades entre microsservicos (RNF-006) nem acoplar-se a um provedor de e-mail (SMTP/SES/SendGrid) que ainda nao esta configurado no projeto.

## Decisao

O `auth-service` **nao envia e-mail diretamente**. Ao gerar um token de confirmacao de e-mail ou de redefinicao de senha, ele publica um evento na exchange `gabaritai.notifications` (topic, durable) declarada em `RabbitMqConfig`:

| Routing key | Quando |
|---|---|
| `email.confirmation-requested` | apos cadastro (UC-001) ou reenvio de confirmacao |
| `email.password-reset-requested` | apos solicitacao de recuperacao de senha (UC-003) |

> **Nota (2026-07-16):** a tabela acima descreve a implementacao original com RabbitMQ. Apos o [ADR-0003](0003-troca-rabbitmq-por-kafka.md), o broker e Kafka e as "routing keys" viraram valores do campo `eventType` no payload, publicados em um topico unico `gabaritai.notifications`. Mantido aqui para registro historico da decisao original.

Payload (`NotificationEvent`): `eventType`, `recipientEmail`, `recipientName`, `token` (valor bruto, de uso unico), `expiresAt`, `occurredAt`.

O banco do `auth-service` armazena **apenas o hash SHA-256** do token (RNF-031); o valor bruto existe somente em memoria e no corpo do evento publicado — nunca em log (RNF-064).

Como o `notification-service` ainda nao existe, essas mensagens ficam na exchange sem consumidor ate que ele seja implementado. Isso e aceitavel porque:

* a exchange e durable e a fila de destino (quando criada pelo notification-service) tambem devera ser durable, entao nenhuma mensagem publicada apos a criacao da fila sera perdida;
* o fluxo de cadastro/login do `auth-service` nao fica bloqueado esperando confirmacao de entrega (publicacao e fire-and-forget do ponto de vista do dominio de autenticacao);
* localmente, o RabbitMQ Management UI (`http://localhost:15672`) permite inspecionar as mensagens publicadas durante o desenvolvimento, ate o consumidor existir.

## Alternativas consideradas

* **auth-service envia e-mail diretamente (SMTP/SES)** — violaria RNF-006 (responsabilidade de negocio bem definida) ao misturar autenticacao com entrega de notificacoes, e exigiria credenciais de um provedor de e-mail que ainda nao foi escolhido/configurado no projeto. Rejeitada.
* **Retornar o token na resposta da API** — inseguro: qualquer requisicao de cadastro ou de "esqueci minha senha" revelaria o token de ativacao/redefinicao no proprio response, tornando trivial ativar contas de terceiros ou sequestrar senhas. Rejeitada.
* **Chamada REST sincrona ao (futuro) notification-service** — contraria RNF-008, que exige mensageria para envio de notificacoes, e acoplaria a disponibilidade do cadastro/login a disponibilidade do notification-service. Rejeitada.

## Consequencias

* Ate o `notification-service` ser implementado, nenhum e-mail real e enviado; a confirmacao de conta e a redefinicao de senha nao podem ser concluidas fora de testes automatizados (que consomem a fila diretamente via Testcontainers) ou inspecao manual da fila. Isso e uma limitacao temporaria, nao um problema de design.
* Qualquer novo fluxo que precise notificar o usuario por e-mail deve seguir o mesmo padrao (publicar em `gabaritai.notifications`, nunca enviar e-mail diretamente de um servico de dominio).
* O contrato do evento (`NotificationEvent`) passa a ser um contrato publico entre servicos; mudanças nele devem ser retrocompativeis (RNF-097) uma vez que o notification-service exista.
