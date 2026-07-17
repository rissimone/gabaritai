# ADR-0003 — Troca de RabbitMQ por Apache Kafka na mensageria

**Status:** aceita
**Data:** 2026-07-16
**Requisitos relacionados:** RNF-008, RNF-053, RNF-054, seção 18 ("Mensageria") de `docs/requisitos-nao-funcionais.md`
**Substitui parcialmente:** [ADR-0002](0002-eventos-notificacao-assincrona.md) — apenas a escolha do broker; a decisão de publicar eventos assíncronos em vez de enviar e-mail diretamente continua válida e não foi alterada.

## Contexto

`docs/requisitos-nao-funcionais.md` foi atualizado (seção 18, "Mensageria") para exigir Apache Kafka em vez de RabbitMQ, em função de uma perspectiva de aumento no volume de operações de mensageria do projeto. O `auth-service` já publicava eventos de notificação (confirmação de e-mail, redefinição de senha) em uma exchange RabbitMQ, conforme ADR-0002.

## Decisão

Toda a integração de mensageria do `auth-service` foi trocada de RabbitMQ para Kafka:

* **Broker local**: `docker-compose.yml` agora sobe `apache/kafka:3.9.2` em modo KRaft (broker + controller combinados, sem Zookeeper), na rede `gabaritai-application`, sem porta publicada no host (RNF-018).
* **Tópico**: um único tópico `gabaritai.notifications` (3 partições, fator de replicação 1 — instância única local) substitui a exchange topic `gabaritai.notifications` do RabbitMQ. O campo `eventType` do payload (`NotificationEvent`), que já existia, passa a ser o mecanismo de discriminação de tipo de evento no lado do consumidor — antes, essa função era parcialmente cumprida pela routing key do RabbitMQ.
* **Chave de particionamento**: as mensagens são publicadas com o e-mail do destinatário como chave Kafka, preservando ordem por usuário dentro da partição (equivalente ao que a routing key + fila única do RabbitMQ garantia).
* **Serialização**: `spring.json.add.type.headers=false` — o contrato entre serviços é o JSON do `NotificationEvent`, não o nome da classe Java do produtor (evita acoplar consumidores futuros ao pacote Java interno do `auth-service`).
* **Dependências**: `spring-boot-starter-amqp` → `spring-kafka`; `org.testcontainers:rabbitmq` → `org.testcontainers:kafka` (testes usam a imagem `confluentinc/cp-kafka`, mantida separada da imagem `apache/kafka` usada em produção/local — ambas falam o protocolo Kafka padrão; a escolha por Confluent nos testes segue a documentação oficial do Testcontainers, mais testada nesse cenário).

O restante da arquitetura definida no ADR-0002 (auth-service não envia e-mail diretamente, token bruto só existe em memória e no evento, notification-service ainda não implementado) permanece inalterado.

## Alternativas consideradas

* **Manter RabbitMQ e reavaliar apenas quando o volume realmente crescer** — mais simples operacionalmente hoje, mas contraria a decisão já registrada na documentação de requisitos, que antecipa o crescimento e prefere pagar o custo de complexidade do Kafka desde já para evitar uma migração de broker mais invasiva no futuro, quando já houver múltiplos produtores/consumidores em produção.
* **Kafka com Zookeeper** — modo legado, mais componentes para manter; KRaft é o modo recomendado pelo próprio projeto Apache Kafka desde a versão 3.3+ e reduz a infraestrutura local a um único container.
* **Um tópico por tipo de evento** (`email-confirmation-requested`, `password-reset-requested`) em vez de um tópico único com campo `eventType` — mais alinhado a convenções comuns de Kafka, mas desproporcional para os 2 tipos de evento existentes hoje; pode ser revisitado quando mais tipos de evento (e mais serviços produtores) existirem.

## Consequências

* Ambiente local passa a exigir Kafka em vez de RabbitMQ; não há mais interface de administração equivalente ao RabbitMQ Management UI provisionada por padrão (pode-se usar `kafka-console-consumer.sh` dentro do container, ou uma ferramenta como Kafka UI/Redpanda Console em iteração futura de observabilidade).
* Kafka roda sem autenticação (PLAINTEXT) neste estágio, isolado apenas pela rede Docker `gabaritai-application`; produção exigirá SASL/ACLs antes de qualquer exposição além do ambiente local (pendência a ser tratada quando a infraestrutura de produção for definida).
* Qualquer novo serviço que precise publicar ou consumir eventos assíncronos deve seguir o mesmo padrão: tópico compartilhado, chave de particionamento por entidade relevante, sem headers de tipo Java.
