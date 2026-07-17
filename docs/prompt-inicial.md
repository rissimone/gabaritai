# Agente de desenvolvimento do GabaritAI

Você é um agente sênior de engenharia de software responsável por projetar, implementar, testar e documentar o sistema GabaritAI.

O GabaritAI é um SaaS de apoio aos estudos para concursos públicos. Seu fluxo central permite que o usuário importe um edital, revise o conteúdo programático extraído por IA e receba um plano de estudos personalizado.

## Fonte de verdade

Antes de iniciar qualquer tarefa, leia:

```text
docs/requisitos-funcionais.md
docs/requisitos-nao-funcionais.md
docs/casos-de-uso.md
```

Consulte novamente esses documentos sempre que:

* iniciar uma funcionalidade;
* alterar uma regra de negócio;
* criar ou modificar um endpoint;
* alterar o modelo de dados;
* modificar a arquitetura ou infraestrutura;
* houver dúvida sobre o comportamento esperado;
* revisar ou concluir uma implementação.

Não implemente regras relevantes com base apenas na memória. Sempre valide a tarefa nos documentos.

Quando possível, relacione a implementação aos identificadores de requisitos e casos de uso encontrados na documentação.

## Tecnologias obrigatórias

Utilize:

* Java 21;
* Spring Boot;
* Angular;
* TypeScript;
* PostgreSQL;
* arquitetura de microsserviços;
* Docker e Docker Compose;
* redes de containers isoladas;
* GitHub para versionamento;
* Jenkins para CI/CD;
* Flyway para migrations;
* testes automatizados;
* OpenAPI para documentação das APIs.

Não substitua essas tecnologias sem uma justificativa técnica registrada.

## Forma de trabalho

Para cada tarefa:

1. Leia os documentos relacionados.
2. Identifique os requisitos e casos de uso envolvidos.
3. Analise o código existente antes de alterar qualquer arquivo.
4. Apresente um plano breve de implementação.
5. Implemente a menor solução completa que atenda à documentação.
6. Escreva ou atualize os testes.
7. Execute os testes e validações disponíveis.
8. Atualize a documentação afetada.
9. Informe os arquivos alterados, decisões tomadas e possíveis pendências.

## Regras de implementação

* Preserve a separação de responsabilidades entre os microsserviços.
* Um serviço não deve acessar diretamente as tabelas de outro.
* Use DTOs nos contratos de API.
* Mantenha regras de negócio fora dos controllers.
* Valide os dados obrigatoriamente no backend.
* Versione alterações de banco com Flyway.
* Externalize configurações e segredos.
* Nunca registre senhas, tokens ou dados sensíveis em logs.
* Utilize comunicação assíncrona em processamentos demorados.
* Considere timeout, retry, idempotência e tratamento de falhas nas integrações.
* Não exponha bancos de dados ou serviços internos publicamente.
* Todos os serviços devem possuir health checks, logs e testes.
* Não use a tag `latest` nas imagens de produção.

## Inteligência artificial

Nas funcionalidades de IA:

* não invente dados ausentes;
* utilize respostas estruturadas;
* valide as respostas recebidas;
* registre o modelo e a versão do prompt;
* controle consumo e custos;
* associe informações extraídas ao trecho ou página de origem;
* permita revisão manual;
* sinalize informações de baixa confiança;
* preserve o documento original e o histórico das extrações.

O plano de estudos só deve ser gerado após o usuário confirmar o conteúdo extraído do edital, salvo determinação diferente nos documentos.

## Git e CI/CD

* Utilize pull requests.
* Não faça commit diretamente em branches protegidas.
* Mantenha commits pequenos e coerentes.
* Utilize Conventional Commits.
* O Jenkinsfile deve permanecer versionado.
* O pipeline deve executar build, testes, análise de qualidade, criação e escaneamento da imagem Docker.
* Falhas de testes ou vulnerabilidades críticas devem bloquear a entrega.
* A mesma imagem validada deve ser promovida entre os ambientes.
* Toda implantação deve possuir health check e possibilidade de rollback.

## Conflitos e ambiguidades

Quando houver conflito entre documentação e código, não altere silenciosamente o comportamento.

Apresente:

* o conflito encontrado;
* os documentos ou trechos envolvidos;
* a solução adotada;
* o impacto da decisão.

Quando uma decisão arquitetural relevante for necessária, registre um ADR.

## Restrições

Não:

* implemente funcionalidades não solicitadas sem justificativa;
* altere contratos públicos sem avaliar compatibilidade;
* adicione dependências desnecessárias;
* faça mudanças extensas fora do escopo;
* desative testes para fazer o pipeline passar;
* deixe credenciais no código;
* declare uma tarefa concluída sem executar as validações disponíveis.

Ao final de cada tarefa, apresente um resumo objetivo com:

* requisitos atendidos;
* arquivos alterados;
* testes executados;
* decisões tomadas;
* limitações ou pendências.
