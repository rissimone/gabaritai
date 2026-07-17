A proposta do **GabaritAI** pode ser organizada em torno de uma jornada principal: o candidato importa o edital, informa sua disponibilidade e recebe um plano de estudos personalizado, acompanhado e ajustado pela IA conforme seu desempenho.

## 1. Requisitos funcionais principais

### RF01 — Cadastro e autenticação

O sistema deve permitir:

* cadastro por e-mail e senha;
* login com Google;
* recuperação de senha;
* confirmação de e-mail;
* edição de perfil;
* exclusão da conta;
* aceite dos termos de uso e política de privacidade.

### RF02 — Perfil do candidato

O usuário deve poder informar:

* concurso de interesse;
* cargo pretendido;
* data prevista da prova;
* nível de conhecimento por disciplina;
* quantidade de horas disponíveis por dia;
* dias da semana disponíveis;
* horários preferenciais;
* experiência anterior com concursos;
* dificuldades de aprendizagem;
* meta de aprovação ou classificação;
* preferência por teoria, questões, revisão ou conteúdo em vídeo.

Essas informações servirão como base para a personalização do plano.

### RF03 — Cadastro do concurso

O sistema deve permitir que o usuário cadastre um concurso manualmente, informando:

* órgão;
* banca organizadora;
* cargo;
* número do edital;
* data da prova;
* escolaridade;
* disciplinas;
* quantidade de vagas;
* link oficial do concurso;
* situação do concurso: previsto, edital publicado, inscrições abertas ou prova realizada.

### RF04 — Importação do edital

O usuário deve poder importar o edital em formatos como:

* PDF;
* documento de texto;
* imagem;
* link para o edital;
* texto copiado e colado.

O sistema deve validar:

* formato do arquivo;
* tamanho máximo;
* legibilidade do documento;
* existência de senha ou bloqueio no PDF;
* presença de conteúdo programático.

### RF05 — Leitura e interpretação do edital por IA

A IA deve identificar automaticamente:

* órgão;
* banca;
* cargo;
* data da prova;
* disciplinas;
* tópicos e subtópicos;
* pesos das disciplinas;
* quantidade de questões;
* critérios de aprovação;
* conteúdos eliminatórios;
* etapas do concurso;
* datas importantes;
* regras específicas;
* conhecimentos gerais e específicos.

O sistema deve apresentar os dados extraídos para que o usuário revise antes de confirmar.

### RF06 — Correção manual da análise

O usuário deve poder:

* editar disciplinas;
* adicionar ou remover tópicos;
* corrigir nomes;
* reorganizar conteúdos;
* alterar pesos;
* informar prioridades;
* marcar conteúdos que não serão estudados;
* confirmar a versão final do conteúdo programático.

Isso é importante porque a leitura da IA pode não ser totalmente precisa.

## 2. Geração do plano de estudos

### RF07 — Configuração da disponibilidade

O usuário deve informar:

* horas disponíveis por dia;
* dias disponíveis;
* intervalos;
* horários fixos;
* compromissos recorrentes;
* dias de descanso;
* período máximo de estudo por sessão;
* tempo disponível até a prova.

### RF08 — Geração automática do plano

A IA deve criar um plano com base em:

* conteúdo programático;
* data da prova;
* tempo disponível;
* peso de cada disciplina;
* nível de conhecimento informado;
* dificuldade dos assuntos;
* histórico de desempenho;
* incidência dos assuntos na banca;
* quantidade estimada de conteúdo;
* necessidade de revisões e simulados.

O plano deve indicar:

* disciplina;
* assunto;
* tipo de atividade;
* duração;
* data;
* prioridade;
* objetivo da sessão;
* material sugerido;
* número de questões recomendado.

### RF09 — Tipos de atividade

O plano deve poder incluir:

* estudo teórico;
* leitura de legislação;
* resolução de questões;
* revisão;
* produção de resumos;
* flashcards;
* simulados;
* correção de erros;
* estudo de jurisprudência;
* treino de redação;
* treino discursivo;
* preparação física, quando aplicável.

### RF10 — Plano por ciclos de estudos

O sistema deve permitir planos:

* por calendário;
* por ciclo de disciplinas;
* por metas semanais;
* por blocos de tempo;
* por prioridade;
* híbridos.

No estudo por ciclo, o candidato avança para a próxima disciplina independentemente do dia da semana.

### RF11 — Personalização do plano

O usuário deve poder:

* alterar a duração das sessões;
* mover atividades;
* excluir atividades;
* trocar disciplinas;
* aumentar ou reduzir a prioridade;
* adicionar compromissos;
* bloquear horários;
* adicionar conteúdos extras;
* solicitar um novo plano à IA.

### RF12 — Replanejamento automático

Quando o usuário atrasar ou adiantar o cronograma, o sistema deve:

* identificar atividades não concluídas;
* redistribuir o conteúdo;
* preservar assuntos prioritários;
* recalcular revisões;
* ajustar simulados;
* evitar sobrecarga;
* informar os impactos da mudança;
* pedir confirmação antes de substituir o plano.

## 3. Acompanhamento dos estudos

### RF13 — Registro das sessões

O usuário deve poder registrar:

* atividade concluída;
* tempo real de estudo;
* número de questões resolvidas;
* número de acertos;
* nível de dificuldade;
* nível de concentração;
* assunto estudado;
* observações;
* materiais utilizados.

### RF14 — Cronômetro de estudos

O sistema deve oferecer:

* cronômetro simples;
* técnica Pomodoro;
* pausas configuráveis;
* registro automático da sessão;
* bloqueio de distrações, quando possível;
* notificações de início e fim;
* histórico de tempo estudado.

### RF15 — Painel de desempenho

O painel deve exibir:

* horas estudadas;
* cumprimento do plano;
* percentual do edital concluído;
* desempenho por disciplina;
* desempenho por assunto;
* taxa de acertos;
* evolução semanal;
* constância;
* tempo médio por questão;
* disciplinas mais fortes;
* disciplinas mais fracas;
* atividades atrasadas;
* previsão de conclusão do edital.

### RF16 — Mapa do edital

O sistema deve apresentar o edital de forma visual, indicando:

* não iniciado;
* em andamento;
* estudado;
* revisado;
* dominado;
* com dificuldade;
* pendente de questões;
* pendente de revisão.

### RF17 — Metas

O usuário deve poder definir metas como:

* horas por semana;
* número de questões;
* disciplinas concluídas;
* simulados realizados;
* percentual do edital;
* taxa mínima de acertos.

O sistema deve acompanhar o progresso e emitir alertas.

## 4. Revisões inteligentes

### RF18 — Agenda de revisões

O sistema deve programar revisões com base em intervalos como:

* 24 horas;
* 7 dias;
* 15 dias;
* 30 dias;
* intervalo personalizado.

### RF19 — Repetição espaçada

A IA deve ajustar a frequência das revisões conforme:

* desempenho do usuário;
* dificuldade informada;
* taxa de acertos;
* tempo desde o último contato;
* incidência do conteúdo;
* proximidade da prova.

### RF20 — Caderno de erros

O sistema deve permitir:

* registrar questões erradas;
* categorizar o motivo do erro;
* vincular o erro a uma disciplina;
* adicionar comentários;
* gerar revisões;
* marcar o erro como superado;
* identificar padrões recorrentes.

Os motivos podem incluir:

* desconhecimento do conteúdo;
* falta de atenção;
* interpretação incorreta;
* confusão entre conceitos;
* erro de cálculo;
* falta de tempo.

## 5. Questões, simulados e desempenho

### RF21 — Banco de questões

O sistema deve permitir filtrar questões por:

* banca;
* órgão;
* cargo;
* disciplina;
* assunto;
* dificuldade;
* ano;
* nível de escolaridade;
* questões já respondidas;
* questões erradas;
* questões favoritas.

Caso utilize questões de terceiros, será necessário observar licenciamento e direitos de uso.

### RF22 — Resolução de questões

Durante a resolução, o usuário deve poder:

* selecionar uma resposta;
* visualizar o gabarito;
* consultar comentário;
* solicitar explicação da IA;
* salvar a questão;
* adicionar ao caderno de erros;
* reportar erro;
* registrar tempo de resposta.

### RF23 — Explicação por IA

A IA deve poder:

* explicar por que uma alternativa está correta;
* explicar por que as demais estão erradas;
* simplificar o conteúdo;
* apresentar exemplos;
* comparar conceitos;
* gerar um resumo;
* sugerir tópicos relacionados;
* criar novas questões semelhantes.

A interface deve deixar claro que a explicação pode conter imprecisões e deve ser confrontada com fontes oficiais.

### RF24 — Simulados

O sistema deve gerar simulados com base em:

* banca;
* concurso;
* cargo;
* disciplinas;
* peso das matérias;
* quantidade de questões;
* tempo de prova;
* nível de dificuldade;
* tópicos já estudados;
* tópicos do edital completo.

### RF25 — Correção de simulados

Após o simulado, o sistema deve apresentar:

* nota;
* percentual de acertos;
* tempo total;
* tempo por questão;
* desempenho por disciplina;
* assuntos com mais erros;
* comparação com simulados anteriores;
* recomendação de estudo;
* plano de recuperação.

## 6. Recursos de inteligência artificial

### RF26 — Tutor virtual

O usuário deve poder conversar com um tutor especializado no concurso para:

* tirar dúvidas;
* pedir explicações;
* solicitar exemplos;
* gerar resumos;
* criar mapas mentais;
* comparar conceitos;
* elaborar questões;
* montar revisões;
* planejar a semana;
* interpretar trechos do edital.

### RF27 — Geração de conteúdo

A IA deve poder gerar:

* resumos;
* flashcards;
* perguntas e respostas;
* mapas mentais;
* listas de revisão;
* questões inéditas;
* simulados;
* tabelas comparativas;
* exemplos práticos;
* planos de recuperação.

### RF28 — Recomendações personalizadas

O sistema deve recomendar:

* disciplina que deve ser priorizada;
* assunto para revisar;
* quantidade de questões;
* momento de realizar simulados;
* necessidade de reduzir ou aumentar a carga;
* conteúdos com risco de esquecimento;
* mudanças no plano.

### RF29 — Análise da banca

A plataforma pode apresentar:

* assuntos mais cobrados;
* estilo de questão;
* nível médio de dificuldade;
* tipos de pegadinha;
* padrão de cobrança;
* distribuição histórica por disciplina;
* recomendações de preparação.

Essa funcionalidade dependerá da existência de dados confiáveis e legalmente utilizáveis.

## 7. Materiais de estudo

### RF30 — Biblioteca de materiais

O usuário deve poder cadastrar:

* PDFs;
* links;
* videoaulas;
* livros;
* apostilas;
* legislação;
* anotações;
* resumos;
* cursos externos.

### RF31 — Vinculação de material ao edital

Cada material poderá ser vinculado a:

* concurso;
* disciplina;
* tópico;
* atividade;
* sessão de estudo;
* revisão.

### RF32 — Anotações

O sistema deve permitir:

* criar anotações;
* formatar texto;
* adicionar imagens;
* destacar trechos;
* inserir links;
* criar tags;
* pesquisar conteúdo;
* vincular anotações a tópicos do edital.

### RF33 — Flashcards

O usuário deve poder:

* criar flashcards manualmente;
* gerar flashcards com IA;
* organizar por disciplina;
* avaliar facilidade ou dificuldade;
* revisar por repetição espaçada;
* importar e exportar cartões.

## 8. Calendário e notificações

### RF34 — Calendário de estudos

O calendário deve exibir:

* sessões planejadas;
* revisões;
* simulados;
* datas do concurso;
* prazos de inscrição;
* compromissos pessoais;
* atividades atrasadas.

### RF35 — Integração com calendários

O sistema poderá integrar-se com:

* Google Calendar;
* Outlook Calendar;
* Apple Calendar.

### RF36 — Notificações

O sistema deve enviar notificações sobre:

* início da sessão;
* atividade atrasada;
* revisão pendente;
* meta semanal;
* simulado agendado;
* prazo de inscrição;
* pagamento de inscrição;
* divulgação de local de prova;
* aproximação da prova.

Os canais podem incluir:

* notificação no sistema;
* e-mail;
* push;
* WhatsApp, como recurso premium ou integração futura.

## 9. Gamificação

### RF37 — Pontos e conquistas

O sistema pode conceder pontos por:

* cumprir o plano;
* estudar em dias consecutivos;
* realizar revisões;
* resolver questões;
* melhorar o desempenho;
* concluir disciplinas;
* realizar simulados.

### RF38 — Sequência de estudos

O usuário deve visualizar:

* dias consecutivos estudados;
* melhor sequência;
* consistência mensal;
* dias de pausa planejados.

A gamificação deve incentivar sem gerar culpa ou pressão excessiva.

## 10. Assinaturas e monetização

### RF39 — Planos de assinatura

O sistema pode oferecer:

**Plano gratuito**

* um concurso ativo;
* importação limitada de edital;
* plano de estudos básico;
* limite de interações com IA;
* acompanhamento simplificado.

**Plano premium**

* múltiplos concursos;
* replanejamento ilimitado;
* tutor com IA;
* simulados;
* análises avançadas;
* flashcards automáticos;
* relatórios detalhados;
* integrações.

### RF40 — Pagamentos

O sistema deve permitir:

* assinatura mensal;
* assinatura anual;
* período de teste;
* cupom de desconto;
* cancelamento;
* alteração de plano;
* renovação automática;
* emissão de comprovante;
* consulta ao histórico de pagamentos.

### RF41 — Controle de consumo da IA

A plataforma deve registrar:

* quantidade de interações;
* documentos processados;
* tokens consumidos;
* limites por plano;
* custo estimado por usuário;
* bloqueio ou redução de uso após atingir o limite.

## 11. Administração da plataforma

### RF42 — Painel administrativo

O administrador deve poder gerenciar:

* usuários;
* assinaturas;
* concursos;
* editais;
* disciplinas;
* questões;
* simulados;
* conteúdos;
* cupons;
* notificações;
* denúncias;
* solicitações de suporte;
* consumo de IA.

### RF43 — Gestão de editais

A equipe administrativa deve poder:

* cadastrar editais;
* revisar extrações realizadas pela IA;
* corrigir conteúdos;
* publicar modelos validados;
* versionar alterações;
* definir um edital como oficial;
* reutilizar o edital para vários usuários.

Isso pode reduzir custos, pois o mesmo edital não precisará ser processado várias vezes.

### RF44 — Gestão de prompts

O administrador deve poder:

* cadastrar prompts;
* versionar prompts;
* testar resultados;
* definir modelos de IA;
* monitorar custos;
* comparar qualidade;
* ativar ou desativar funcionalidades.

### RF45 — Auditoria

O sistema deve registrar:

* alterações no edital;
* modificações no plano;
* ações administrativas;
* uso da IA;
* acessos;
* falhas;
* pagamentos;
* consentimentos.

## 12. Regras de negócio importantes

### RN01 — Confirmação do conteúdo extraído

Nenhum plano definitivo deve ser gerado antes de o usuário confirmar o conteúdo extraído do edital.

### RN02 — Alteração da data da prova

Quando a data da prova mudar, o sistema deve recalcular o plano e informar o impacto.

### RN03 — Conteúdo não identificado

Quando a IA não conseguir interpretar parte do edital, o sistema deve:

* sinalizar o trecho;
* solicitar revisão;
* evitar inventar informações;
* permitir inclusão manual.

### RN04 — Fontes oficiais

Datas, regras e critérios do concurso devem ser vinculados ao trecho ou página do edital de origem sempre que possível.

### RN05 — Atualização do edital

Quando houver retificação:

* a versão anterior deve ser preservada;
* as alterações devem ser destacadas;
* o usuário deve ser avisado;
* o plano deve ser recalculado após confirmação.

### RN06 — Limites de estudo

O sistema deve evitar planos inviáveis, como:

* carga diária excessiva;
* ausência total de descanso;
* quantidade incompatível com o tempo disponível;
* sessões muito longas;
* excesso de disciplinas no mesmo dia.

### RN07 — Transparência da IA

Conteúdos gerados por IA devem ser identificados como tal. Informações críticas devem recomendar consulta ao edital e às fontes oficiais.

## 14. MVP recomendado

Para a primeira versão, eu priorizaria:

1. Cadastro e login.
2. Cadastro do concurso.
3. Upload do edital em PDF.
4. Extração do conteúdo programático por IA.
5. Tela de revisão e correção dos dados.
6. Configuração da disponibilidade semanal.
7. Geração do plano de estudos.
8. Calendário de atividades.
9. Marcação de atividades como concluídas.
10. Registro de tempo e questões.
11. Painel básico de progresso.
12. Replanejamento de atividades atrasadas.
13. Plano gratuito e assinatura premium.
14. Painel administrativo básico.
15. Controle de custos e limites da IA.
