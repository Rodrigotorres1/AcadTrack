# BDD — Behavior Driven Development

## Visão geral

O AcadTrack adota BDD para especificar e validar o comportamento do sistema a partir da perspectiva de negócio. Cada cenário é escrito em linguagem natural (português, sintaxe Gherkin) e executado automaticamente contra a aplicação real — com banco H2 em memória, contexto Spring completo e MockMvc para chamadas HTTP. A suite cobre **77 cenários distribuídos em 14 features**.

---

## Ferramentas e configuração

| Componente | Versão / detalhe |
|---|---|
| Cucumber | 7.18.1 (`cucumber-java`, `cucumber-junit-platform-engine`, `cucumber-spring`) |
| JUnit Platform Suite | gerenciado pelo Spring Boot BOM |
| Spring Boot Test | contexto completo com `@SpringBootTest(webEnvironment = MOCK)` |
| Banco de dados | H2 em memória, schema gerado por `ddl-auto=create-drop` |
| Runner | `CucumberTest` com `@Suite @IncludeEngines("cucumber") @SelectClasspathResource("features")` |
| Glue | `g8.acadtrack.bdd` (step definitions + configuração Spring) |

### Estrutura do módulo `bdd/acadtrackbdd`

```
src/test/
├── java/g8/acadtrack/bdd/
│   ├── CucumberTest.java                   ← runner JUnit Platform Suite
│   ├── CucumberSpringConfiguration.java    ← @CucumberContextConfiguration + @SpringBootTest
│   ├── steps/                              ← 14 classes de step definitions
│   └── unit/                              ← 5 classes de testes unitários (JUnit puro)
└── resources/
    ├── application.properties              ← H2 + ddl-auto=create-drop
    └── features/
        ├── analise_desempenho.feature
        ├── cadastro_email.feature
        ├── criar_simulado.feature
        ├── desempenho_reprovado.feature
        ├── excluir_responsavel.feature
        ├── gestao_disciplina.feature
        ├── lancar_nota.feature
        ├── notificacao_risco_academico.feature
        ├── retificacao_guards.feature
        ├── solicitar_retificacao_nota.feature
        ├── vincular_responsavel.feature
        └── extra/
            ├── calcular_media_ponderada.feature
            ├── gerar_ranking.feature
            └── vincular_aluno_turma.feature
```

---

## Resumo das features

| # | Feature | Cenários | Bounded Context |
|---|---------|----------|-----------------|
| 1 | `analise_desempenho` | 6 | Acadêmico / Avaliação |
| 2 | `cadastro_email` | 5 | Compartilhado |
| 3 | `criar_simulado` | 8 | Avaliação |
| 4 | `desempenho_reprovado` | 1 | Acadêmico / Avaliação |
| 5 | `excluir_responsavel` | 3 | Usuários |
| 6 | `gestao_disciplina` | 4 | Avaliação |
| 7 | `lancar_nota` | 9 | Avaliação |
| 8 | `notificacao_risco_academico` | 6 | Usuários / Avaliação |
| 9 | `retificacao_guards` | 2 | Avaliação |
| 10 | `solicitar_retificacao_nota` | 16 | Avaliação |
| 11 | `vincular_responsavel` | 9 | Usuários / Acadêmico |
| 12 | `extra/calcular_media_ponderada` | 1 | Avaliação |
| 13 | `extra/gerar_ranking` | 3 | Acadêmico |
| 14 | `extra/vincular_aluno_turma` | 4 | Acadêmico |
| | **Total** | **77** | |

---

## Detalhamento por feature

### 1. Análise consolidada de desempenho acadêmico (6 cenários)

**Contexto:** valida o caso de uso `AnalisarDesempenhoAcademicoUseCase`, que agrega média geral, nível de risco e posição no ranking em um único objeto de análise.

| Cenário | O que testa |
|---------|------------|
| Gerar análise consolidada sem risco acadêmico | Aluno com histórico consistente recebe risco BAIXO e `possuiRisco = false` |
| Gerar alerta de risco por média geral baixa | Média < 5,0 com 2+ simulados ruins classifica como ALTO |
| Gerar alerta de risco por baixo desempenho em simulado | Média < 6,0 com exatamente 1 simulado ruim classifica como MODERADO |
| Exibir posição acadêmica do aluno dentro da análise | Posição no ranking é incorporada à análise; aluno na 2ª posição aparece no Top 10 |
| Não gerar análise para aluno sem notas | `RegraDeNegocioException` lançada quando não há notas |
| Retornar erro 400 na API para aluno sem notas | `GET /analise/{alunoId}` responde 400 com mensagem legível |

**Justificativa:** cobre o caminho feliz de cada nível de risco (BAIXO, MODERADO, ALTO), a integração com ranking e os dois pontos de entrada — domínio direto e API REST.

---

### 2. Validar email nos cadastros (5 cenários)

**Contexto:** valida o Value Object `Email`, que normaliza e rejeita valores inválidos na criação de `Aluno` e `Responsavel`.

| Cenário | O que testa |
|---------|------------|
| Não cadastrar aluno sem email | Campo nulo/vazio lança `RegraDeNegocioException("Email é obrigatório")` |
| Não cadastrar aluno com email inválido | Formato sem `@dominio.tld` é rejeitado |
| Não cadastrar aluno com e-mail duplicado por capitalização | `"Alice@email.com"` e `" alice@email.com "` são tratados como o mesmo endereço |
| Não cadastrar responsável sem email | Mesma regra aplicada ao contexto de `Responsavel` |
| Não cadastrar responsável com email inválido | Domínio sem TLD válido (`sem-dominio`) é rejeitado |

**Justificativa:** o `Email` é um Value Object compartilhado entre dois bounded contexts; os cenários cobrem as duas entidades que o usam e a invariante mais crítica — normalização por capitalização — que evita duplicatas silenciosas.

---

### 3. Criar simulado com disciplinas vinculadas (8 cenários)

**Contexto:** valida `CriarSimuladoUseCase` e `AtualizarSimuladoUseCase`, incluindo restrições de composição e imutabilidade pós-notas.

| Cenário | O que testa |
|---------|------------|
| Criar simulado com disciplinas vinculadas | Happy path: simulado com 2 disciplinas ativas é criado |
| Não permitir criar simulado sem disciplinas | Lista vazia lança exceção de negócio |
| Não permitir criar simulado com apenas uma disciplina | API rejeita payload com 1 disciplina (400) |
| Não permitir disciplina repetida na composição | Duplicata na lista é detectada e rejeitada |
| Não permitir criar simulado com disciplina inexistente | ID inválido lança `EntidadeNaoEncontradaException` |
| Não permitir criar simulado com descrição duplicada | Descrição case-insensitive verifica unicidade |
| Não permitir alterar disciplinas de simulado com notas lançadas | Imutabilidade pós-lançamento protege consistência das notas existentes |
| Não permitir atualizar simulado com disciplina inativa | Disciplina `INATIVA` não pode entrar em nova composição |

**Justificativa:** cobre todas as regras de invariante do agregado `Simulado` — composição mínima, unicidade de disciplinas, unicidade de descrição e o guard de imutabilidade que protege dados históricos.

---

### 4. Desempenho reprovado (1 cenário)

**Contexto:** verifica a integração entre `AnalisarDesempenhoAcademicoUseCase` e a situação acadêmica calculada pelo domínio.

| Cenário | O que testa |
|---------|------------|
| Classificar situação acadêmica como REPROVADO para média abaixo de 5.0 | Média < 5,0 resulta em `situacaoAcademica = REPROVADO` e `nivelRisco = ALTO` na análise |

**Justificativa:** feature isolada para evidenciar explicitamente que reprovação e risco ALTO são consequências simultâneas da mesma condição de média, separando essa combinação crítica dos demais cenários de lançamento.

---

### 5. Excluir responsável (3 cenários)

**Contexto:** valida `ExcluirResponsavelUseCase`, incluindo o efeito colateral de limpeza de vínculo no `Aluno`.

| Cenário | O que testa |
|---------|------------|
| Excluir responsável sem aluno vinculado | Exclusão simples sem efeito colateral |
| Excluir responsável com aluno vinculado limpa o vínculo do aluno | `removerResponsavel()` é chamado no aluno antes de deletar |
| Não permitir excluir responsável inexistente | ID inexistente lança `EntidadeNaoEncontradaException` |

**Justificativa:** os três cenários cobrem as duas pré-condições possíveis (com e sem aluno) e o caso de erro. O cenário de vínculo é o mais crítico: garante que a exclusão não deixa o estado do `Aluno` inconsistente.

---

### 6. Gestão de disciplinas com restrições acadêmicas (4 cenários)

**Contexto:** valida `CriarDisciplinaUseCase` e as restrições de uso de disciplinas `INATIVA` em notas e simulados.

| Cenário | O que testa |
|---------|------------|
| Criar disciplina com status inicial ativa | Toda disciplina nasce com `status = ATIVA` |
| Não permitir disciplina duplicada por nome normalizado | Nomes com espaços extras e capitalização diferente são detectados como duplicatas |
| Impedir lançamento de nota para disciplina inativa | `LancarNotaUseCase` rejeita disciplina `INATIVA` |
| Impedir uso de disciplina inativa em simulado | `CriarSimuladoUseCase` rejeita disciplina `INATIVA` na composição |

**Justificativa:** a normalização de nomes (trim + lowercase) é uma invariante de unicidade; os cenários de disciplina inativa garantem que o status `INATIVA` propaga seu efeito de guarda para os dois contextos que a consomem.

---

### 7. Lançar nota individual (9 cenários)

**Contexto:** valida `LancarNotaUseCase`, incluindo o recálculo automático de média e a atualização de `SituacaoAcademica`.

| Cenário | O que testa |
|---------|------------|
| Lançar nota válida | Happy path: nota entre 0 e 10 é persistida |
| Lançar nota via API sem enriquecimento | `POST /notas` retorna nota sem nome de disciplina/simulado preenchidos |
| Não permitir nota inválida | Nota > 10 é rejeitada com mensagem de negócio |
| Não permitir lançamento para aluno inativo | Aluno com `ativo = false` não recebe novas notas |
| Não permitir nota duplicada | Mesmo aluno + simulado + disciplina lança `ConflitoDeEstadoException` |
| Recalcular média automaticamente após novo lançamento | Após 2ª nota, média é atualizada para a média aritmética das duas |
| Definir situação acadêmica no limite de recuperação (média 5,0) | média exata 5,0 resulta em `RECUPERACAO` |
| Definir situação acadêmica como REPROVADO para média abaixo de 5,0 | média < 5,0 resulta em `REPROVADO` |
| Definir situação acadêmica no limite de aprovação (média 7,0) | média ≥ 7,0 resulta em `APROVADO` |

**Justificativa:** os três cenários de situação acadêmica testam especificamente os limiares de transição (5,0 e 7,0), que são as fronteiras mais sensíveis do modelo de negócio e onde erros de comparação (`<` vs `<=`) causariam regressões silenciosas.

---

### 8. Notificação automática de responsáveis sobre risco acadêmico (6 cenários)

**Contexto:** valida o fluxo Observer: `LancarNotaUseCase` → `RiscoAcademicoEvent` → `NotificarResponsavelRiscoAcademicoHandler` → `NotificacaoResponsavel`.

| Cenário | O que testa |
|---------|------------|
| Criar notificação para risco ALTO | Notificação criada com `nivelRisco = ALTO` e `prioridade = ALTA`, status `NAO_LIDA` |
| Criar notificação para risco MODERADO | Notificação criada com `nivelRisco = MODERADO` e `prioridade = MEDIA` |
| Não criar notificação para risco BAIXO | Nenhuma notificação persiste quando risco é BAIXO |
| Não falhar quando aluno em risco não tem responsável | `RiscoAcademicoEvent` é publicado mas handler ignora ausência de responsável sem lançar exceção |
| Listar notificações de um responsável | `GET /notificacoes/responsavel/{id}` retorna lista correta |
| Marcar notificação como lida | `PATCH /notificacoes/{id}/lida` atualiza `status = LIDA` |

**Justificativa:** o padrão Observer desacopla o lançamento de nota da notificação; os cenários garantem que a integração entre os bounded contexts funciona ponta a ponta — do evento de domínio até a persistência e consulta da notificação.

---

### 9. Guards de retificação (2 cenários)

**Contexto:** valida as transições de estado proibidas no ciclo de vida de `SolicitacaoRetificacao`.

| Cenário | O que testa |
|---------|------------|
| Impedir transição de APROVADA para EM_ANALISE | Solicitação já aprovada não pode regredir para análise |
| Impedir transição de REPROVADA para EM_ANALISE | Solicitação já reprovada não pode regredir para análise |

**Justificativa:** estes cenários isolam as guardas de estado terminal (`APROVADA`, `REPROVADA`) em feature própria para evidenciar que a máquina de estados é irrevogável nos estados finais, complementando os fluxos normais descritos em `solicitar_retificacao_nota`.

---

### 10. Solicitar retificação de nota (16 cenários)

**Contexto:** valida o ciclo de vida completo de `SolicitacaoRetificacao` — criação, transição de estado, aprovação, reprovação e listagem enriquecida.

| Cenário | O que testa |
|---------|------------|
| Solicitar retificação com justificativa válida | Happy path: solicitação registrada com status `PENDENTE` |
| Não permitir solicitação sem justificativa | Justificativa vazia lança exceção de negócio |
| Não permitir retificação para nota inexistente | ID de nota inválido retorna `EntidadeNaoEncontradaException` |
| Não permitir múltiplas solicitações em aberto para a mesma nota | Segunda solicitação com status aberto (`PENDENTE` ou `EM_ANALISE`) é bloqueada |
| Não permitir iniciar análise de retificação inexistente | ID inválido retorna `EntidadeNaoEncontradaException` na transição |
| Permitir transição PENDENTE → EM_ANALISE | Status atualizado corretamente para `EM_ANALISE` |
| Não permitir aprovar retificação inexistente | ID inválido retorna `EntidadeNaoEncontradaException` na aprovação |
| Não permitir aprovar retificação com status PENDENTE | Aprovação sem passar por `EM_ANALISE` é bloqueada |
| Aprovar retificação atualiza nota e situação acadêmica | Aprovação persiste novo valor de nota e recalcula `SituacaoAcademica` |
| Reprovar retificação não altera a nota | Reprovação mantém valor original intacto |
| Não permitir reprovar retificação inexistente | ID inválido retorna `EntidadeNaoEncontradaException` |
| Não permitir reprovar retificação já aprovada | Estado terminal `APROVADA` bloqueia reprovação subsequente |
| Não permitir aprovar sem justificativa de decisão | Campo `justificativaDecisao` é obrigatório na aprovação |
| Não permitir reprovar sem justificativa de decisão | Campo `justificativaDecisao` é obrigatório na reprovação |
| Permitir aluno inativo solicitar retificação | Inatividade não impede revisão de notas históricas |
| Listar retificações retorna dados enriquecidos | Listagem inclui nome de aluno, disciplina e simulado via join no repositório |

**Justificativa:** feature com maior cobertura porque `SolicitacaoRetificacao` possui uma máquina de estados não trivial (PENDENTE → EM_ANALISE → APROVADA/REPROVADA) com múltiplas guardas. Cada transição válida e inválida é testada individualmente para garantir que nenhuma regressão de estado seja introduzida silenciosamente.

---

### 11. Vincular ou desvincular responsável (9 cenários)

**Contexto:** valida o padrão Proxy — `AcessoResponsavelAlunoProxy` pré-valida permissão antes de delegar — e as operações de vínculo em `VincularResponsavelUseCase` / `DesvincularResponsavelUseCase`.

| Cenário | O que testa |
|---------|------------|
| Vincular responsável ao aluno | Happy path: vínculo registrado com permissões padrão |
| Não permitir duplicidade de vínculo ativo | Segunda vinculação do mesmo responsável ativo lança `ConflitoDeEstadoException` |
| Não permitir desvincular responsável inexistente | Desvinculação sem vínculo ativo lança exceção |
| Impedir acesso sem vínculo ativo | Proxy bloqueia consulta de notas quando não há vínculo |
| Impedir acesso a simulados após desvincular | Desvinculação revoga acesso a simulados imediatamente |
| Impedir acesso a desempenho após desvincular | Desvinculação revoga acesso a desempenho imediatamente |
| Impedir responsável sem permissão VISUALIZAR_NOTAS | Proxy bloqueia acesso a notas por permissão insuficiente |
| Impedir acesso a simulados sem permissão | Proxy bloqueia acesso a simulados por permissão insuficiente |
| Impedir acesso a desempenho sem permissão | Proxy bloqueia acesso a desempenho por permissão insuficiente |

**Justificativa:** os 6 cenários de controle de acesso (3 de vínculo + 3 de permissão) validam explicitamente o Proxy de proteção — cada tipo de recurso protegido (notas, simulados, desempenho) tem seu próprio cenário tanto para ausência de vínculo quanto para permissão insuficiente.

---

### 12. Calcular média por simulado (1 cenário)

**Contexto:** valida `CalcularMediaPorSimuladoUseCase` com composição de pesos definida em `SimuladoDisciplina`.

| Cenário | O que testa |
|---------|------------|
| Calcular média com composição padrão do simulado | Média ponderada é calculada corretamente para as disciplinas da composição padrão |

**Justificativa:** cenário de smoke test para o algoritmo de cálculo de média ponderada, isolado para evidenciar a regra de negócio de composição sem a complexidade do fluxo de lançamento completo.

---

### 13. Gerar ranking (3 cenários)

**Contexto:** valida `GerarRankingAcademicoUseCase`, que usa o padrão Iterator (`RankingAcademicoIterator`) para limitar e ordenar resultados.

| Cenário | O que testa |
|---------|------------|
| Gerar ranking com alunos ordenados | Alunos são retornados do maior para o menor desempenho |
| Não gerar ranking sem notas | Lista vazia retornada sem exceção quando não há notas |
| Ranking usa o mesmo risco da análise de desempenho | Nível de risco no item de ranking é consistente com o calculado pela análise consolidada |

**Justificativa:** o terceiro cenário é o mais importante — garante consistência entre dois casos de uso que calculam risco independentemente, evitando que o aluno apareça com risco diferente dependendo do endpoint consultado.

---

### 14. Vincular aluno à turma (4 cenários)

**Contexto:** valida `VincularAlunoTurmaUseCase` e `LimparTurmasDuplicadasUseCase`.

| Cenário | O que testa |
|---------|------------|
| Vincular aluno a uma turma com sucesso | Happy path: turma associada ao aluno |
| Trocar turma de aluno já matriculado | Substituição de turma sem criar segundo vínculo simultâneo |
| Não permitir criar turma duplicada | Nome normalizado é verificado antes de persistir |
| Limpar turmas duplicadas migrando alunos | Use case de manutenção remove duplicata e redireciona alunos |

**Justificativa:** o cenário de limpeza de duplicatas testa um caso operacional real — migração de dados sem perda de vínculo — relevante em contextos onde turmas foram criadas com formatação inconsistente.

---

## Justificativa da cobertura

### Por que 77 cenários

A distribuição de cenários por feature é proporcional à complexidade do modelo de domínio:

- **`solicitar_retificacao_nota` (16):** maior contagem porque a máquina de estados tem 4 estados e as transições inválidas superam as válidas — cada guard não testado é um bug potencial em produção.
- **`lancar_nota` (9) e `vincular_responsavel` (9):** operações com efeitos colaterais amplos (recálculo de média, eventos de domínio, controle de acesso) que precisam de cobertura de limiares e permutações.
- **`criar_simulado` (8):** agregado com múltiplas invariantes de composição que precisam ser verificadas individualmente.
- **`analise_desempenho` (6) e `notificacao_risco_academico` (6):** casos de uso que integram múltiplos bounded contexts; cada nível de risco e cada condição de borda ganha cenário próprio.
- **Features de 1 a 4 cenários:** funcionalidades com lógica mais direta (CRUD com poucas regras) ou cenários isolados para evidenciar uma invariante específica.

### O que não está coberto por BDD

Os seguintes comportamentos são validados por testes unitários na pasta `unit/`, não por cenários BDD, por não envolverem colaboração entre contextos:

- Algoritmo de ordenação do ranking (`OrdenarRankingAcademicoServiceTest`)
- Lógica das três strategies de risco isoladamente (`RiscoAcademicoStrategyTest`)
- Serviço de avaliação acadêmica (`AvaliacaoAcademicaServiceTest`)
- Value Object `Email` (`EmailTest`)
- Caso de uso de análise sem ranking (`AnalisarRiscoAcademicoSemRankingUseCaseTest`)

Testes unitários validam a **correção interna** de cada componente; os cenários BDD validam o **comportamento observável** do sistema como um todo.
