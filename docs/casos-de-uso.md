# Casos de Uso — GabaritAI

## 1. Atores do sistema

### Ator: Candidato

Usuário principal da plataforma, responsável por cadastrar concursos, importar editais, configurar sua rotina e acompanhar o plano de estudos.

### Ator: Administrador

Responsável pela administração da plataforma, gestão de usuários, editais, planos, assinaturas, conteúdos e configurações.

### Ator: Serviço de Inteligência Artificial

Responsável por interpretar editais, gerar planos de estudo, produzir explicações, resumos, revisões e recomendações.

### Ator: Serviço de Pagamento

Sistema externo responsável pelo processamento de assinaturas, cobranças, renovações e cancelamentos.

### Ator: Serviço de Notificação

Responsável pelo envio de e-mails, notificações push e demais alertas.

### Ator: Sistema de Armazenamento

Responsável pelo armazenamento de editais, materiais de estudo, relatórios e arquivos enviados pelos usuários.

---

# 2. Casos de uso do candidato

## UC-001 — Cadastrar usuário

**Ator principal:** Candidato

**Objetivo:** Criar uma conta na plataforma.

**Pré-condições:** O usuário não deve possuir cadastro com o mesmo e-mail.

**Fluxo principal:**

1. O candidato acessa a tela de cadastro.
2. Informa nome, e-mail e senha.
3. Aceita os termos de uso e a política de privacidade.
4. O sistema valida os dados.
5. O sistema cria a conta.
6. O sistema envia um e-mail de confirmação.
7. O candidato confirma o cadastro.

**Fluxos alternativos:**

* O e-mail informado já está cadastrado.
* A senha não atende aos critérios mínimos.
* O e-mail de confirmação não é recebido.
* O candidato solicita o reenvio da confirmação.

**Pós-condições:** A conta é criada e pode ser utilizada após a confirmação.

---

## UC-002 — Autenticar usuário

**Ator principal:** Candidato

**Objetivo:** Acessar a plataforma.

**Pré-condições:** O candidato deve possuir uma conta ativa.

**Fluxo principal:**

1. O candidato informa e-mail e senha.
2. O sistema valida as credenciais.
3. O sistema gera a sessão ou token de acesso.
4. O candidato é direcionado ao painel principal.

**Fluxos alternativos:**

* Credenciais inválidas.
* Conta não confirmada.
* Conta bloqueada.
* Excesso de tentativas de login.

**Pós-condições:** O candidato acessa a área autenticada.

---

## UC-003 — Recuperar senha

**Ator principal:** Candidato

**Objetivo:** Redefinir a senha da conta.

**Fluxo principal:**

1. O candidato informa o e-mail cadastrado.
2. O sistema envia um link de recuperação.
3. O candidato acessa o link.
4. Informa uma nova senha.
5. O sistema valida e atualiza a senha.

**Pós-condições:** A nova senha passa a ser válida.

---

## UC-004 — Editar perfil

**Ator principal:** Candidato

**Objetivo:** Atualizar informações pessoais e preferências.

**Fluxo principal:**

1. O candidato acessa o perfil.
2. Altera os dados desejados.
3. O sistema valida as informações.
4. O sistema salva as alterações.

**Dados possíveis:**

* nome;
* foto;
* telefone;
* fuso horário;
* preferência de notificações;
* nível de experiência;
* objetivo de estudo;
* preferências de aprendizagem.

---

## UC-005 — Excluir conta

**Ator principal:** Candidato

**Objetivo:** Solicitar a exclusão da conta e dos dados pessoais.

**Fluxo principal:**

1. O candidato acessa as configurações.
2. Solicita a exclusão da conta.
3. O sistema solicita confirmação.
4. O candidato confirma.
5. O sistema bloqueia o acesso.
6. O sistema inicia o processo de exclusão ou anonimização dos dados.

**Pós-condições:** A conta é desativada e os dados são tratados conforme a política de retenção.

---

# 3. Casos de uso de concursos e editais

## UC-006 — Cadastrar concurso

**Ator principal:** Candidato

**Objetivo:** Registrar um concurso de interesse.

**Fluxo principal:**

1. O candidato acessa a área de concursos.
2. Seleciona a opção de cadastrar concurso.
3. Informa órgão, cargo, banca e data da prova.
4. O sistema valida os dados.
5. O concurso é salvo.

**Fluxos alternativos:**

* O concurso já existe na plataforma.
* A data da prova não foi definida.
* O candidato cadastra o concurso como previsto.

---

## UC-007 — Visualizar concursos cadastrados

**Ator principal:** Candidato

**Objetivo:** Consultar os concursos associados à conta.

**Fluxo principal:**

1. O candidato acessa a área de concursos.
2. O sistema apresenta os concursos cadastrados.
3. O candidato pode filtrar por status.
4. O candidato seleciona um concurso para visualizar seus detalhes.

---

## UC-008 — Editar concurso

**Ator principal:** Candidato

**Objetivo:** Atualizar informações de um concurso.

**Fluxo principal:**

1. O candidato seleciona o concurso.
2. Altera as informações.
3. O sistema valida os dados.
4. O sistema salva as alterações.
5. Caso a data da prova seja alterada, o sistema informa que o plano poderá ser recalculado.

---

## UC-009 — Excluir concurso

**Ator principal:** Candidato

**Objetivo:** Remover um concurso da conta.

**Fluxo principal:**

1. O candidato seleciona o concurso.
2. Solicita a exclusão.
3. O sistema informa os impactos.
4. O candidato confirma.
5. O sistema remove ou arquiva o concurso.

---

## UC-010 — Importar edital

**Ator principal:** Candidato

**Atores secundários:** Sistema de Armazenamento e Serviço de Inteligência Artificial

**Objetivo:** Enviar um edital para análise.

**Pré-condições:** O candidato deve estar autenticado e possuir um concurso cadastrado.

**Fluxo principal:**

1. O candidato seleciona um concurso.
2. Escolhe a opção de importar edital.
3. Seleciona um arquivo PDF.
4. O sistema valida o formato e o tamanho.
5. O arquivo é armazenado.
6. O processamento é iniciado.
7. O candidato acompanha o status da importação.

**Fluxos alternativos:**

* Arquivo em formato inválido.
* Arquivo acima do tamanho permitido.
* PDF protegido por senha.
* Arquivo corrompido.
* Documento sem conteúdo programático identificável.

**Pós-condições:** O edital fica disponível para processamento.

---

## UC-011 — Processar edital com IA

**Ator principal:** Serviço de Inteligência Artificial

**Objetivo:** Extrair informações relevantes do edital.

**Pré-condições:** O edital deve ter sido importado com sucesso.

**Fluxo principal:**

1. O sistema obtém o arquivo armazenado.
2. Extrai o texto do documento.
3. Identifica dados gerais do concurso.
4. Identifica disciplinas, tópicos e subtópicos.
5. Identifica datas, etapas, pesos e critérios.
6. Registra a página ou trecho de origem.
7. Armazena os dados extraídos.
8. Atualiza o status do processamento.
9. Notifica o candidato.

**Fluxos alternativos:**

* O texto não pode ser extraído.
* É necessário aplicar OCR.
* Parte do conteúdo apresenta baixa confiabilidade.
* O processamento é interrompido.
* O limite de consumo da IA é atingido.

**Pós-condições:** Os dados extraídos ficam disponíveis para revisão.

---

## UC-012 — Revisar conteúdo extraído do edital

**Ator principal:** Candidato

**Objetivo:** Confirmar ou corrigir as informações obtidas pela IA.

**Pré-condições:** O processamento do edital deve estar concluído.

**Fluxo principal:**

1. O candidato acessa o resultado da extração.
2. O sistema apresenta os dados identificados.
3. O candidato revisa as informações.
4. Corrige, adiciona ou remove conteúdos.
5. Confirma a versão final.
6. O sistema registra a versão validada.

**Pós-condições:** O conteúdo programático é aprovado para a geração do plano.

---

## UC-013 — Consultar edital estruturado

**Ator principal:** Candidato

**Objetivo:** Visualizar o edital de forma organizada.

**Fluxo principal:**

1. O candidato seleciona o concurso.
2. Acessa o edital estruturado.
3. O sistema apresenta disciplinas, tópicos e subtópicos.
4. O candidato pode filtrar, pesquisar e consultar o trecho original.

---

## UC-014 — Importar retificação do edital

**Ator principal:** Candidato

**Atores secundários:** Serviço de Inteligência Artificial

**Objetivo:** Atualizar o edital a partir de uma retificação.

**Fluxo principal:**

1. O candidato seleciona o concurso.
2. Importa o documento de retificação.
3. O sistema processa o novo documento.
4. A IA compara o documento com a versão anterior.
5. O sistema destaca inclusões, remoções e alterações.
6. O candidato revisa e confirma.
7. O sistema cria uma nova versão do edital.

**Pós-condições:** O edital é atualizado e o plano pode ser recalculado.

---

# 4. Casos de uso do planejamento de estudos

## UC-015 — Informar disponibilidade de estudos

**Ator principal:** Candidato

**Objetivo:** Configurar os períodos disponíveis para estudo.

**Fluxo principal:**

1. O candidato acessa a configuração de disponibilidade.
2. Informa os dias da semana.
3. Informa os horários disponíveis.
4. Define o tempo máximo por sessão.
5. Informa pausas e dias de descanso.
6. O sistema salva a disponibilidade.

---

## UC-016 — Informar nível de conhecimento

**Ator principal:** Candidato

**Objetivo:** Classificar o nível de domínio em cada disciplina.

**Fluxo principal:**

1. O sistema apresenta as disciplinas do edital.
2. O candidato informa o nível de conhecimento.
3. O candidato pode indicar dificuldades específicas.
4. O sistema registra as respostas.

**Níveis sugeridos:**

* nenhum conhecimento;
* básico;
* intermediário;
* avançado;
* domínio elevado.

---

## UC-017 — Definir meta de estudos

**Ator principal:** Candidato

**Objetivo:** Registrar metas de preparação.

**Fluxo principal:**

1. O candidato seleciona o concurso.
2. Define metas semanais ou mensais.
3. Informa horas, questões, revisões ou simulados.
4. O sistema valida a viabilidade.
5. O sistema salva as metas.

---

## UC-018 — Gerar plano de estudos

**Ator principal:** Candidato

**Ator secundário:** Serviço de Inteligência Artificial

**Objetivo:** Criar um plano personalizado.

**Pré-condições:**

* O conteúdo do edital deve estar confirmado.
* A disponibilidade deve estar configurada.
* A data da prova deve estar informada ou estimada.

**Fluxo principal:**

1. O candidato solicita a geração do plano.
2. O sistema reúne os dados do concurso.
3. O sistema considera a disponibilidade.
4. O sistema considera os níveis de conhecimento.
5. A IA calcula prioridades.
6. A IA distribui conteúdos, revisões e questões.
7. O sistema apresenta uma prévia.
8. O candidato confirma o plano.
9. O plano é ativado.

**Fluxos alternativos:**

* O tempo disponível é insuficiente.
* A data da prova não está definida.
* O edital não foi validado.
* O serviço de IA está indisponível.
* O limite de geração foi atingido.

**Pós-condições:** O candidato possui um plano ativo.

---

## UC-019 — Visualizar plano de estudos

**Ator principal:** Candidato

**Objetivo:** Consultar as atividades planejadas.

**Fluxo principal:**

1. O candidato acessa o plano.
2. O sistema apresenta as atividades por dia, semana ou ciclo.
3. O candidato pode visualizar disciplina, conteúdo, duração e prioridade.
4. O candidato pode filtrar atividades por status.

---

## UC-020 — Editar plano de estudos

**Ator principal:** Candidato

**Objetivo:** Alterar atividades do plano.

**Fluxo principal:**

1. O candidato seleciona uma atividade.
2. Altera data, duração, disciplina ou tipo.
3. O sistema valida o impacto.
4. O candidato confirma.
5. O sistema atualiza o plano.

---

## UC-021 — Reorganizar atividades

**Ator principal:** Candidato

**Objetivo:** Mover atividades entre datas ou ciclos.

**Fluxo principal:**

1. O candidato seleciona uma atividade.
2. Escolhe uma nova data ou posição.
3. O sistema verifica conflitos.
4. O sistema salva a nova organização.

---

## UC-022 — Replanejar cronograma

**Ator principal:** Candidato

**Ator secundário:** Serviço de Inteligência Artificial

**Objetivo:** Redistribuir atividades devido a atrasos ou mudanças de disponibilidade.

**Fluxo principal:**

1. O candidato solicita o replanejamento.
2. O sistema identifica atividades pendentes.
3. O sistema considera a nova disponibilidade.
4. A IA recalcula as prioridades.
5. O sistema apresenta o novo plano.
6. O candidato confirma a alteração.

**Fluxos alternativos:**

* O candidato recusa o novo plano.
* Não existe tempo suficiente até a prova.
* Existem atividades prioritárias que não podem ser redistribuídas.

---

## UC-023 — Arquivar plano de estudos

**Ator principal:** Candidato

**Objetivo:** Encerrar um plano sem excluí-lo.

**Fluxo principal:**

1. O candidato seleciona o plano.
2. Solicita o arquivamento.
3. O sistema solicita confirmação.
4. O plano é marcado como arquivado.

---

# 5. Casos de uso de execução e acompanhamento

## UC-024 — Iniciar sessão de estudo

**Ator principal:** Candidato

**Objetivo:** Iniciar uma atividade planejada.

**Fluxo principal:**

1. O candidato seleciona uma atividade.
2. Inicia a sessão.
3. O sistema inicia o cronômetro.
4. O sistema registra o horário de início.

---

## UC-025 — Pausar sessão de estudo

**Ator principal:** Candidato

**Objetivo:** Pausar temporariamente uma sessão.

**Fluxo principal:**

1. O candidato seleciona a opção de pausa.
2. O sistema interrompe o cronômetro.
3. O tempo de pausa é registrado.
4. O candidato pode retomar a sessão.

---

## UC-026 — Finalizar sessão de estudo

**Ator principal:** Candidato

**Objetivo:** Encerrar uma atividade de estudo.

**Fluxo principal:**

1. O candidato encerra a sessão.
2. O sistema calcula o tempo total.
3. O candidato informa o conteúdo estudado.
4. Informa questões resolvidas e acertos.
5. Registra dificuldade e observações.
6. O sistema salva a sessão.

---

## UC-027 — Marcar atividade como concluída

**Ator principal:** Candidato

**Objetivo:** Registrar a conclusão de uma atividade.

**Fluxo principal:**

1. O candidato seleciona a atividade.
2. Marca como concluída.
3. O sistema solicita informações complementares.
4. O sistema atualiza o progresso.
5. O sistema agenda revisões, quando necessário.

---

## UC-028 — Marcar atividade como não concluída

**Ator principal:** Candidato

**Objetivo:** Registrar que uma atividade planejada não foi realizada.

**Fluxo principal:**

1. O candidato seleciona a atividade.
2. Marca como não concluída.
3. Informa opcionalmente o motivo.
4. O sistema inclui a atividade na lista de pendências.
5. O sistema sugere replanejamento.

---

## UC-029 — Registrar estudo não planejado

**Ator principal:** Candidato

**Objetivo:** Registrar uma atividade realizada fora do plano.

**Fluxo principal:**

1. O candidato seleciona a opção de registro manual.
2. Informa disciplina, conteúdo e duração.
3. Registra questões, acertos e observações.
4. O sistema adiciona a atividade ao histórico.
5. O progresso é atualizado.

---

## UC-030 — Visualizar progresso

**Ator principal:** Candidato

**Objetivo:** Acompanhar a evolução nos estudos.

**Fluxo principal:**

1. O candidato acessa o painel.
2. O sistema apresenta horas estudadas.
3. Apresenta percentual do edital concluído.
4. Apresenta taxa de acertos.
5. Exibe evolução por disciplina.
6. Exibe metas atingidas e pendentes.

---

## UC-031 — Consultar mapa do edital

**Ator principal:** Candidato

**Objetivo:** Visualizar o estado de cada conteúdo programático.

**Fluxo principal:**

1. O candidato seleciona o mapa do edital.
2. O sistema apresenta disciplinas e tópicos.
3. Cada tópico é identificado por status.
4. O candidato pode filtrar conteúdos pendentes, estudados ou dominados.

---

## UC-032 — Consultar histórico de estudos

**Ator principal:** Candidato

**Objetivo:** Visualizar sessões e atividades anteriores.

**Fluxo principal:**

1. O candidato acessa o histórico.
2. O sistema apresenta as sessões realizadas.
3. O candidato pode filtrar por data, disciplina ou tipo.
4. O candidato pode visualizar detalhes de cada sessão.

---

# 6. Casos de uso de revisões

## UC-033 — Agendar revisão

**Ator principal:** Sistema

**Objetivo:** Criar revisões automáticas após uma atividade concluída.

**Fluxo principal:**

1. Uma atividade é marcada como concluída.
2. O sistema verifica a regra de revisão.
3. Gera as próximas datas.
4. Adiciona as revisões ao plano.
5. O candidato é notificado.

---

## UC-034 — Realizar revisão

**Ator principal:** Candidato

**Objetivo:** Executar uma atividade de revisão.

**Fluxo principal:**

1. O candidato acessa a revisão agendada.
2. Inicia a atividade.
3. Revisa o conteúdo.
4. Registra o nível de domínio.
5. O sistema calcula a próxima revisão.

---

## UC-035 — Avaliar dificuldade do conteúdo

**Ator principal:** Candidato

**Objetivo:** Informar o grau de dificuldade após estudo ou revisão.

**Fluxo principal:**

1. O sistema solicita uma avaliação.
2. O candidato classifica a dificuldade.
3. O sistema atualiza a frequência das revisões.
4. O sistema ajusta a prioridade do conteúdo.

---

## UC-036 — Gerar revisão inteligente

**Ator principal:** Serviço de Inteligência Artificial

**Objetivo:** Produzir material de revisão com base no conteúdo estudado.

**Fluxo principal:**

1. O sistema identifica o tópico.
2. Recupera anotações e histórico.
3. A IA gera resumo, perguntas ou flashcards.
4. O material é apresentado ao candidato.
5. O candidato pode salvar o conteúdo.

---

# 7. Casos de uso de questões e simulados

## UC-037 — Pesquisar questões

**Ator principal:** Candidato

**Objetivo:** Localizar questões de interesse.

**Fluxo principal:**

1. O candidato acessa o banco de questões.
2. Informa filtros.
3. O sistema apresenta os resultados.
4. O candidato seleciona uma questão.

**Filtros possíveis:**

* banca;
* órgão;
* cargo;
* disciplina;
* assunto;
* ano;
* dificuldade;
* status da questão.

---

## UC-038 — Resolver questão

**Ator principal:** Candidato

**Objetivo:** Responder uma questão.

**Fluxo principal:**

1. O candidato visualiza a questão.
2. Seleciona uma alternativa.
3. Confirma a resposta.
4. O sistema informa o resultado.
5. O desempenho é registrado.

---

## UC-039 — Solicitar explicação da questão

**Ator principal:** Candidato

**Ator secundário:** Serviço de Inteligência Artificial

**Objetivo:** Obter uma explicação sobre uma questão.

**Fluxo principal:**

1. O candidato solicita uma explicação.
2. A IA analisa o enunciado e as alternativas.
3. O sistema apresenta a justificativa.
4. O candidato pode solicitar uma explicação simplificada.

---

## UC-040 — Adicionar questão ao caderno de erros

**Ator principal:** Candidato

**Objetivo:** Registrar uma questão respondida incorretamente.

**Fluxo principal:**

1. O candidato seleciona a questão.
2. Escolhe adicionar ao caderno de erros.
3. Informa o motivo do erro.
4. Adiciona uma anotação.
5. O sistema salva o registro.

---

## UC-041 — Consultar caderno de erros

**Ator principal:** Candidato

**Objetivo:** Revisar questões erradas.

**Fluxo principal:**

1. O candidato acessa o caderno de erros.
2. O sistema apresenta as questões registradas.
3. O candidato filtra por disciplina ou motivo.
4. O candidato revisa ou responde novamente.

---

## UC-042 — Gerar simulado

**Ator principal:** Candidato

**Ator secundário:** Serviço de Inteligência Artificial

**Objetivo:** Criar um simulado personalizado.

**Fluxo principal:**

1. O candidato seleciona o concurso.
2. Define quantidade de questões e duração.
3. Escolhe disciplinas ou edital completo.
4. O sistema seleciona ou gera as questões.
5. O simulado é criado.

---

## UC-043 — Realizar simulado

**Ator principal:** Candidato

**Objetivo:** Executar uma prova simulada.

**Fluxo principal:**

1. O candidato inicia o simulado.
2. O sistema inicia o cronômetro.
3. O candidato responde às questões.
4. O candidato finaliza ou o tempo se encerra.
5. O sistema registra as respostas.

---

## UC-044 — Corrigir simulado

**Ator principal:** Sistema

**Objetivo:** Calcular o resultado do simulado.

**Fluxo principal:**

1. O simulado é finalizado.
2. O sistema compara as respostas com o gabarito.
3. Calcula nota, acertos, erros e tempo.
4. Gera análise por disciplina.
5. Atualiza o desempenho do candidato.
6. Recomenda conteúdos para revisão.

---

## UC-045 — Consultar resultado do simulado

**Ator principal:** Candidato

**Objetivo:** Visualizar o desempenho obtido.

**Fluxo principal:**

1. O candidato seleciona um simulado concluído.
2. O sistema apresenta a nota.
3. Exibe desempenho por disciplina.
4. Exibe questões erradas.
5. Apresenta recomendações de estudo.

---

# 8. Casos de uso do tutor com IA

## UC-046 — Enviar pergunta ao tutor

**Ator principal:** Candidato

**Ator secundário:** Serviço de Inteligência Artificial

**Objetivo:** Tirar uma dúvida relacionada aos estudos.

**Fluxo principal:**

1. O candidato acessa o tutor.
2. Digita uma pergunta.
3. O sistema envia o contexto disponível.
4. A IA gera a resposta.
5. O sistema apresenta a resposta.

---

## UC-047 — Solicitar resumo

**Ator principal:** Candidato

**Objetivo:** Gerar um resumo de determinado assunto.

**Fluxo principal:**

1. O candidato informa o conteúdo.
2. Define o nível de detalhamento.
3. A IA produz o resumo.
4. O candidato pode salvar ou editar.

---

## UC-048 — Gerar flashcards

**Ator principal:** Candidato

**Ator secundário:** Serviço de Inteligência Artificial

**Objetivo:** Criar flashcards de um conteúdo.

**Fluxo principal:**

1. O candidato seleciona uma disciplina ou tópico.
2. Solicita a geração.
3. A IA cria perguntas e respostas.
4. O candidato revisa.
5. O candidato salva os flashcards aprovados.

---

## UC-049 — Gerar questões inéditas

**Ator principal:** Candidato

**Ator secundário:** Serviço de Inteligência Artificial

**Objetivo:** Criar questões de treino.

**Fluxo principal:**

1. O candidato informa disciplina e tópico.
2. Seleciona dificuldade e quantidade.
3. A IA gera as questões.
4. O sistema apresenta o conteúdo.
5. O candidato pode responder ou salvar.

---

## UC-050 — Solicitar recomendação de estudo

**Ator principal:** Candidato

**Ator secundário:** Serviço de Inteligência Artificial

**Objetivo:** Receber orientação sobre o que estudar.

**Fluxo principal:**

1. O candidato solicita uma recomendação.
2. O sistema analisa o plano e o desempenho.
3. A IA identifica prioridades.
4. O sistema apresenta sugestões justificadas.

---

# 9. Casos de uso de materiais de estudo

## UC-051 — Cadastrar material

**Ator principal:** Candidato

**Objetivo:** Adicionar um material de estudo.

**Fluxo principal:**

1. O candidato acessa a biblioteca.
2. Seleciona o tipo de material.
3. Envia arquivo ou informa link.
4. Vincula o material a uma disciplina.
5. O sistema salva o cadastro.

---

## UC-052 — Vincular material a um tópico

**Ator principal:** Candidato

**Objetivo:** Associar um material a um item do edital.

**Fluxo principal:**

1. O candidato seleciona o material.
2. Seleciona uma disciplina e um tópico.
3. Confirma a associação.
4. O sistema atualiza o material.

---

## UC-053 — Criar anotação

**Ator principal:** Candidato

**Objetivo:** Registrar uma anotação de estudo.

**Fluxo principal:**

1. O candidato seleciona uma disciplina ou tópico.
2. Cria uma nova anotação.
3. Insere texto, imagens ou links.
4. Salva a anotação.

---

## UC-054 — Pesquisar materiais e anotações

**Ator principal:** Candidato

**Objetivo:** Localizar conteúdos salvos.

**Fluxo principal:**

1. O candidato informa um termo.
2. O sistema pesquisa em materiais e anotações.
3. Apresenta os resultados.
4. O candidato seleciona o conteúdo desejado.

---

# 10. Casos de uso de notificações

## UC-055 — Configurar notificações

**Ator principal:** Candidato

**Objetivo:** Definir quais alertas deseja receber.

**Fluxo principal:**

1. O candidato acessa as configurações.
2. Seleciona os tipos de alerta.
3. Define os canais.
4. Define horários permitidos.
5. O sistema salva as preferências.

---

## UC-056 — Enviar lembrete de estudo

**Ator principal:** Serviço de Notificação

**Objetivo:** Alertar o candidato sobre uma atividade agendada.

**Fluxo principal:**

1. O sistema identifica uma atividade próxima.
2. Consulta as preferências do candidato.
3. Envia a notificação.
4. Registra o envio.

---

## UC-057 — Notificar atividade atrasada

**Ator principal:** Serviço de Notificação

**Objetivo:** Informar que uma atividade não foi concluída.

**Fluxo principal:**

1. O sistema identifica uma atividade vencida.
2. Envia uma notificação.
3. Sugere o replanejamento.

---

## UC-058 — Notificar atualização do edital

**Ator principal:** Serviço de Notificação

**Objetivo:** Informar sobre uma retificação ou alteração.

**Fluxo principal:**

1. O sistema identifica uma nova versão.
2. Verifica os usuários afetados.
3. Envia o alerta.
4. Direciona o candidato para a comparação das versões.

---

# 11. Casos de uso de assinaturas e pagamentos

## UC-059 — Consultar planos de assinatura

**Ator principal:** Candidato

**Objetivo:** Visualizar os planos disponíveis.

**Fluxo principal:**

1. O candidato acessa a área de planos.
2. O sistema apresenta preços e recursos.
3. O candidato compara as opções.

---

## UC-060 — Contratar assinatura

**Ator principal:** Candidato

**Ator secundário:** Serviço de Pagamento

**Objetivo:** Assinar um plano pago.

**Fluxo principal:**

1. O candidato seleciona um plano.
2. Informa os dados de pagamento.
3. O serviço de pagamento processa a cobrança.
4. O sistema recebe a confirmação.
5. A assinatura é ativada.

**Fluxos alternativos:**

* Pagamento recusado.
* Falha de comunicação.
* Dados inválidos.
* Cupom expirado.

---

## UC-061 — Alterar plano

**Ator principal:** Candidato

**Objetivo:** Realizar upgrade ou downgrade.

**Fluxo principal:**

1. O candidato seleciona um novo plano.
2. O sistema apresenta valores e impactos.
3. O candidato confirma.
4. O serviço de pagamento atualiza a cobrança.
5. O sistema altera os limites da conta.

---

## UC-062 — Cancelar assinatura

**Ator principal:** Candidato

**Objetivo:** Interromper a renovação do plano.

**Fluxo principal:**

1. O candidato solicita o cancelamento.
2. O sistema apresenta as consequências.
3. O candidato confirma.
4. O serviço de pagamento cancela a renovação.
5. O sistema informa a data final de acesso.

---

## UC-063 — Consultar histórico de pagamentos

**Ator principal:** Candidato

**Objetivo:** Visualizar cobranças realizadas.

**Fluxo principal:**

1. O candidato acessa o histórico financeiro.
2. O sistema apresenta pagamentos, datas e status.
3. O candidato pode consultar comprovantes.

---

# 12. Casos de uso administrativos

## UC-064 — Autenticar administrador

**Ator principal:** Administrador

**Objetivo:** Acessar o painel administrativo.

**Pré-condições:** O usuário deve possuir permissão administrativa.

---

## UC-065 — Gerenciar usuários

**Ator principal:** Administrador

**Objetivo:** Consultar e administrar contas.

**Ações possíveis:**

* pesquisar usuários;
* visualizar perfil;
* bloquear conta;
* desbloquear conta;
* alterar status;
* consultar assinatura;
* visualizar registros de auditoria.

---

## UC-066 — Gerenciar concursos

**Ator principal:** Administrador

**Objetivo:** Cadastrar, editar e padronizar concursos.

**Ações possíveis:**

* cadastrar concurso;
* alterar informações;
* definir concurso como oficial;
* arquivar concurso;
* associar banca e cargo.

---

## UC-067 — Gerenciar editais

**Ator principal:** Administrador

**Objetivo:** Revisar e manter editais cadastrados.

**Ações possíveis:**

* consultar edital;
* validar extração;
* corrigir conteúdo;
* publicar versão oficial;
* versionar retificações;
* arquivar edital.

---

## UC-068 — Revisar extração da IA

**Ator principal:** Administrador

**Objetivo:** Corrigir dados extraídos com baixa confiabilidade.

**Fluxo principal:**

1. O administrador acessa a fila de revisão.
2. Seleciona um edital.
3. Compara o conteúdo original com a extração.
4. Realiza correções.
5. Aprova a versão.

---

## UC-069 — Gerenciar planos de assinatura

**Ator principal:** Administrador

**Objetivo:** Configurar os planos comerciais.

**Ações possíveis:**

* cadastrar plano;
* definir preço;
* definir limites;
* ativar ou desativar plano;
* configurar período de teste;
* configurar recursos incluídos.

---

## UC-070 — Gerenciar cupons

**Ator principal:** Administrador

**Objetivo:** Criar e controlar descontos.

**Ações possíveis:**

* cadastrar cupom;
* definir validade;
* definir percentual;
* limitar quantidade de usos;
* desativar cupom.

---

## UC-071 — Gerenciar prompts de IA

**Ator principal:** Administrador

**Objetivo:** Configurar os prompts utilizados pela plataforma.

**Ações possíveis:**

* cadastrar prompt;
* editar prompt;
* criar versão;
* testar prompt;
* ativar versão;
* desativar versão;
* associar prompt a uma funcionalidade.

---

## UC-072 — Monitorar consumo de IA

**Ator principal:** Administrador

**Objetivo:** Acompanhar custos e uso dos modelos.

**Fluxo principal:**

1. O administrador acessa o painel de IA.
2. O sistema apresenta consumo por usuário e funcionalidade.
3. Exibe tokens, requisições e custos.
4. O administrador pode definir limites e alertas.

---

## UC-073 — Consultar indicadores da plataforma

**Ator principal:** Administrador

**Objetivo:** Acompanhar métricas de negócio e uso.

**Indicadores possíveis:**

* usuários cadastrados;
* usuários ativos;
* editais processados;
* planos gerados;
* taxa de conversão;
* cancelamentos;
* receita;
* consumo de IA;
* falhas de processamento.

---

## UC-074 — Gerenciar notificações

**Ator principal:** Administrador

**Objetivo:** Criar e enviar comunicações.

**Ações possíveis:**

* criar mensagem;
* selecionar público;
* agendar envio;
* definir canal;
* acompanhar status.

---

## UC-075 — Consultar logs de auditoria

**Ator principal:** Administrador

**Objetivo:** Verificar ações relevantes realizadas no sistema.

**Fluxo principal:**

1. O administrador acessa a auditoria.
2. Aplica filtros.
3. O sistema apresenta os eventos.
4. O administrador consulta os detalhes.

---

# 13. Casos de uso técnicos e automáticos

## UC-076 — Verificar limite de uso da IA

**Ator principal:** Sistema

**Objetivo:** Validar se o usuário pode utilizar uma funcionalidade de IA.

**Fluxo principal:**

1. O usuário solicita uma operação.
2. O sistema consulta o plano contratado.
3. Verifica o consumo atual.
4. Autoriza ou bloqueia a solicitação.
5. Registra o consumo.

---

## UC-077 — Registrar auditoria

**Ator principal:** Sistema

**Objetivo:** Manter o histórico de ações relevantes.

**Eventos possíveis:**

* login;
* exclusão;
* alteração de edital;
* alteração de plano;
* mudança de assinatura;
* ação administrativa;
* processamento de pagamento.

---

## UC-078 — Executar backup

**Ator principal:** Sistema

**Objetivo:** Criar cópias de segurança dos dados.

**Fluxo principal:**

1. O processo é iniciado conforme o agendamento.
2. O sistema gera o backup.
3. Armazena em local seguro.
4. Valida a integridade.
5. Registra o resultado.

---

## UC-079 — Monitorar saúde dos serviços

**Ator principal:** Sistema

**Objetivo:** Verificar a disponibilidade da plataforma.

**Fluxo principal:**

1. O sistema consulta os endpoints de saúde.
2. Verifica banco de dados, filas e serviços.
3. Registra as métricas.
4. Em caso de falha, gera um alerta.

---

## UC-080 — Publicar nova versão

**Ator principal:** Jenkins

**Objetivo:** Realizar integração e entrega contínuas.

**Fluxo principal:**

1. O Jenkins recebe um evento do GitHub.
2. Obtém o código.
3. Compila a aplicação.
4. Executa os testes.
5. Executa a análise de qualidade.
6. Constrói as imagens Docker.
7. Publica as imagens no registry.
8. Realiza a implantação.
9. Executa os testes de saúde.
10. Registra e comunica o resultado.

---

# 14. Casos de uso prioritários para o MVP

Os seguintes casos de uso devem ser priorizados na primeira versão:

1. UC-001 — Cadastrar usuário.
2. UC-002 — Autenticar usuário.
3. UC-003 — Recuperar senha.
4. UC-004 — Editar perfil.
5. UC-006 — Cadastrar concurso.
6. UC-010 — Importar edital.
7. UC-011 — Processar edital com IA.
8. UC-012 — Revisar conteúdo extraído.
9. UC-015 — Informar disponibilidade.
10. UC-016 — Informar nível de conhecimento.
11. UC-018 — Gerar plano de estudos.
12. UC-019 — Visualizar plano de estudos.
13. UC-020 — Editar plano.
14. UC-022 — Replanejar cronograma.
15. UC-024 — Iniciar sessão de estudo.
16. UC-026 — Finalizar sessão.
17. UC-027 — Marcar atividade como concluída.
18. UC-030 — Visualizar progresso.
19. UC-055 — Configurar notificações.
20. UC-056 — Enviar lembrete de estudo.
21. UC-059 — Consultar planos de assinatura.
22. UC-060 — Contratar assinatura.
23. UC-064 — Autenticar administrador.
24. UC-065 — Gerenciar usuários.
25. UC-067 — Gerenciar editais.
26. UC-072 — Monitorar consumo de IA.
27. UC-080 — Publicar nova versão.
