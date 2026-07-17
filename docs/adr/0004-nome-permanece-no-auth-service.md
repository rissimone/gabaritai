# ADR-0004 — "Nome" do candidato permanece no auth-service, não é duplicado no user-service

**Status:** aceita
**Data:** 2026-07-17
**Requisitos relacionados:** RF01, RF02, UC-001, UC-004, RNF-006

## Contexto

UC-004 ("Editar perfil") lista "nome" entre os dados editáveis do perfil do candidato. No entanto, o `auth-service` já armazena `name` desde o cadastro (UC-001), usado para identificação da conta. Implementar "nome" também como campo editável no `user-service` criaria duas fontes de verdade para o mesmo dado, com risco real de divergência (por exemplo, o nome mostrado no cabeçalho da aplicação vindo do JWT/auth-service ficaria desatualizado em relação ao nome editado no perfil).

## Decisão

O campo `name` continua existindo **apenas no `auth-service`**. O `user_profiles` do `user-service` não tem coluna `name`. A edição de nome, quando implementada, deve ser um endpoint do `auth-service` (ex.: `PATCH /api/v1/auth/me`), não do `user-service`.

Os demais campos de UC-004 (foto, telefone, fuso horário, preferência de notificações, nível de experiência, objetivo de estudo, preferências de aprendizagem) são implementados no `user-service`, pois não têm equivalente em nenhum outro serviço.

## Alternativas consideradas

* **Duplicar `name` no `user-service`, sincronizado via evento Kafka do auth-service** — resolveria a consistência eventualmente, mas adiciona um consumidor Kafka, uma tabela e uma lógica de sincronização só para evitar uma segunda escrita — desproporcional para o valor entregue nesta etapa. Pode ser reconsiderado se o `user-service` precisar exibir nome em relatórios/consultas que hoje dependem de agregação no frontend.
* **Mover `name` inteiramente para o `user-service` e remover do `auth-service`** — mais alinhado à separação "auth cuida só de credenciais", mas exigiria alterar o fluxo de cadastro (UC-001) já implementado e testado, sem necessidade concreta agora.

## Consequências

* O frontend usa o `name` retornado pelo login (claim do JWT) para exibição; se um endpoint de edição de nome for adicionado futuramente no auth-service, o frontend precisará atualizar esse valor em memória após a edição (o JWT em si não é reemitido).
* `GET /api/v1/usuarios/perfil` não retorna `name` — quem consumir esse endpoint deve obter o nome separadamente (hoje, do token).
