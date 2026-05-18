# AcadTrack — Plataforma de Gestão Acadêmica

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-007396?logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.14-6DB33F?logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-3.8%2B-C71A36?logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/Cucumber-BDD-23D96C?logo=cucumber&logoColor=white" />
  <img src="https://img.shields.io/badge/JPA%2FHibernate-59666C?logo=hibernate&logoColor=white" />
  <img src="https://img.shields.io/badge/H2-Database-003545" />
</p>

Sistema web para gestão de simulados acadêmicos e acompanhamento do desempenho de alunos. Permite que coordenadores organizem avaliações, professores lancem notas e solicitem retificações, alunos acompanhem seu histórico e responsáveis consultem resultados de alunos vinculados — tudo com regras de negócio não triviais e validações orientadas ao domínio.

---

## Sumário

- [O que o sistema resolve](#o-que-o-sistema-resolve)
- [Arquitetura geral](#arquitetura-geral)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Como rodar](#como-rodar)
- [Endpoints da API](#endpoints-da-api)
- [Regras de negócio principais](#regras-de-negócio-principais)
- [Padrões de projeto](#padrões-de-projeto)
- [Como rodar os testes](#como-rodar-os-testes)
- [Decisões arquiteturais](#decisões-arquiteturais)
- [Glossário do domínio](#glossário-do-domínio)
- [Documentação acadêmica](#documentação-acadêmica)
- [Membros](#membros)

---

## O que o sistema resolve

Instituições de ensino precisam acompanhar o desempenho de alunos ao longo de múltiplas avaliações (simulados), com diferentes disciplinas, pesos e critérios de aprovação. O fluxo manual é sujeito a erros: notas lançadas incorretamente, responsáveis sem acesso organizado às informações, e sem alertas automáticos quando um aluno entra em situação de risco.

O AcadTrack resolve isso com:

- **Criação estruturada de simulados**: cada simulado exige ao menos duas disciplinas distintas e ativas; a composição é validada antes de persistir.
- **Lançamento de notas com efeito imediato**: ao lançar uma nota, o sistema recalcula automaticamente a média global do aluno e atualiza sua situação acadêmica (APROVADO / RECUPERACAO / REPROVADO).
- **Fluxo de retificação**: o aluno pode solicitar revisão de uma nota; a solicitação percorre os estados PENDENTE → EM_ANALISE → APROVADA ou REPROVADA, e aprovações propagam o recálculo de situação.
- **Análise de risco**: após cada lançamento ou aprovação de retificação, o sistema classifica o risco acadêmico do aluno (BAIXO / MODERADO / ALTO) e notifica automaticamente o responsável vinculado quando o risco não é BAIXO.
- **Acesso controlado de responsáveis**: responsáveis só enxergam dados de alunos vinculados a eles, e apenas nas dimensões para as quais têm permissão (notas, simulados, desempenho).

---

## Arquitetura geral

O projeto segue **Clean Architecture** com separação estrita de camadas. As dependências sempre apontam para dentro: a camada de domínio não conhece Spring, JPA nem nenhuma infraestrutura.

```
┌─────────────────────────────────────────────┐
│           apresentacao-backend              │  Controllers REST, DTOs,
│           (Spring Boot + Tomcat)            │  Swagger, tratamento de erros
└────────────────────┬────────────────────────┘
                     │ chama use cases
┌────────────────────▼────────────────────────┐
│                 aplicacao                   │  Use Cases, padrões de projeto,
│             (sem Spring)                    │  orquestração de fluxos
└─────────┬───────────────────────┬───────────┘
          │ acessa interfaces     │ usa entidades
          │ de repositório        │ do domínio
┌─────────▼──────────┐  ┌────────▼────────────┐
│  infraestrutura    │  │  dominio-academico   │
│  (JPA / Hibernate) │  │  dominio-avaliacao   │  Entidades, regras,
│  Spring Data       │  │  dominio-usuarios    │  interfaces de repositório
└────────────────────┘  │  dominio-compartilhado│
                        └──────────────────────┘
```

**Módulos Maven** (todos declarados no `pom.xml` raiz):

| Módulo | Papel |
|---|---|
| `dominio-compartilhado` | Exceções de domínio, `Email`, `NivelRiscoAcademico` |
| `dominio-academico` | `Aluno`, `Turma`, `Disciplina` e suas interfaces de repositório |
| `dominio-avaliacao` | `Nota`, `Simulado`, `SimuladoDisciplina`, `SolicitacaoRetificacao` |
| `dominio-usuarios` | `Responsavel`, `NotificacaoResponsavel` e interfaces |
| `aplicacao` | Todos os use cases, padrões de projeto, serviços de domínio |
| `infraestrutura` | Entidades JPA (`@Entity`), adaptadores de repositório, Spring Data |
| `apresentacao-backend` | Aplicação Spring Boot executável, controllers REST, DTOs, SPA estática |
| `apresentacao-frontend` | Placeholder Maven (`packaging=pom`); arquivos reais ficam em `apresentacao-backend/static/` |
| `bdd/acadtrackbdd` | Testes BDD com Cucumber e Spring Test |

---

## Estrutura de pastas

```
AcadTrack/
│
├── pom.xml                          POM pai: declara todos os 9 módulos e Spring Boot 3.5.14
├── mvnw / mvnw.cmd                  Maven Wrapper — use no lugar de mvn quando não tiver Maven no PATH
│
├── aplicacao/                       Camada de aplicação (use cases, padrões de projeto)
│   └── src/main/java/g8/acadtrack/aplicacao/
│       ├── aluno/                   AtivarAluno, InativarAluno, CriarAluno, AtualizarAluno,
│       │                            BuscarAlunoPorId, ListarAlunos (6 use cases)
│       ├── disciplina/              Criar, Atualizar, Buscar, Listar, Inativar, Ativar, Excluir (7)
│       ├── nota/
│       │   ├── LancarNotaUseCase                  Orquestra validação + persiste + recalcula média
│       │   ├── BuscarNotasPorAlunoUseCase
│       │   ├── CalcularMediaPonderadaUseCase       Média do aluno restrita a um simulado
│       │   ├── AnalisarDesempenhoAcademicoUseCase  Consolida histórico + classifica risco
│       │   ├── AvaliacaoAcademicaService           Recalcula média global simples e SituacaoAcademica
│       │   ├── FluxoAnaliseAcademicaTemplate       PADRÃO: Template Method (4 etapas fixas)
│       │   ├── risco/
│       │   │   ├── EstrategiaClassificacaoRiscoAcademico  (interface — PADRÃO: Strategy)
│       │   │   ├── RiscoAltoStrategy               média < 5
│       │   │   ├── RiscoBaixoStrategy              média >= 7
│       │   │   └── RiscoModeradoStrategy           5 <= média < 7
│       │   └── validacao/
│       │       ├── ValidadorLancamentoNota         (interface do decorador)
│       │       ├── ValidadorLancamentoNotaBase     Elo final da cadeia (sem validação)
│       │       ├── ValidadorLancamentoNotaDecorator  (abstract — PADRÃO: Decorator)
│       │       ├── ValidadorValorNotaDecorator     valor entre 0 e 10
│       │       ├── ValidadorEntidadesLancamentoNotaDecorator  aluno/simulado/disciplina existem
│       │       ├── ValidadorAlunoAtivoDecorator    aluno não pode estar inativo
│       │       ├── ValidadorDisciplinaAtivaDecorator  disciplina não pode estar inativa
│       │       ├── ValidadorDisciplinaVinculadaSimuladoDecorator  disciplina pertence ao simulado
│       │       ├── ValidadorNotaDuplicadaDecorator duplo lançamento (aluno+simulado+disciplina)
│       │       └── ValidacaoLancamentoNotaService  Monta a cadeia e dispara a validação
│       ├── notificacao/             ListarNotificacoesResponsavel, MarcarNotificacaoLida (2)
│       ├── ranking/
│       │   ├── GerarRankingAcademicoUseCase        ranking geral com critério configurável
│       │   ├── GerarRankingUseCase                 ranking de alunos por simulado
│       │   ├── CriterioRankingAcademico            enum (MEDIA_DESC)
│       │   └── OrdenarRankingAcademicoService
│       ├── responsavel/
│       │   ├── AcessoResponsavelAlunoProxy         PADRÃO: Proxy — verifica vínculo/permissão
│       │   ├── ValidarAcessoResponsavelAlunoUseCase
│       │   ├── ConsultarNotasAlunoPorResponsavelUseCase
│       │   ├── ConsultarSimuladosAlunoPorResponsavelUseCase
│       │   ├── ConsultarDesempenhoAlunoPorResponsavelUseCase
│       │   ├── CriarResponsavel, ExcluirResponsavel, ListarResponsaveis
│       │   ├── VincularResponsavel, DesvincularResponsavel (9 use cases)
│       ├── retificacao/             Solicitar, IniciarAnalise, Aprovar, Reprovar,
│       │                            Listar, Detalhar, MontarDetalhe (7 use cases)
│       ├── evento/
│       │   └── DomainEventPublisher                porta para publicação de eventos de domínio
│       ├── riscoacademico/
│       │   └── NotificarResponsavelRiscoAcademicoHandler  cria NotificacaoResponsavel a partir de evento de domínio
│       ├── simulado/                Criar, Atualizar, Detalhar, Listar, ListarComResumo,
│       │                            ValidarComposicao, AnalisarConsistencia (10 use cases/services)
│       └── turma/                  Criar, Listar, VincularAlunoTurma, LimparDuplicadas (4)
│
├── apresentacao-backend/            Módulo Spring Boot — único módulo com main() executável
│   └── src/main/
│       ├── java/g8/acadtrack/apresentacao/
│       │   ├── AcadTrackApplication.java            @SpringBootApplication — ponto de entrada
│       │   ├── config/
│       │   │   ├── CorsConfig.java                  Libera todas as origens para dev local
│       │   │   ├── DadosIniciaisConfig.java          Cria as 6 turmas padrão (1ºA…3ºB) na inicialização
│       │   │   └── OpenApiConfig.java               Configura título/versão do Swagger
│       │   ├── controller/                          8 @RestController (ver seção Endpoints)
│       │   ├── dto/request/                         17 classes de request com @Valid
│       │   └── dto/response/                        16 classes de response com factory fromDomain()
│       │   └── exception/GlobalExceptionHandler     Mapeia exceções de domínio para HTTP
│       └── resources/
│           ├── application.properties               Porta 8080, H2 em arquivo, Swagger, JPA
│           └── static/
│               ├── index.html                       SPA com login por persona e navegação lateral
│               ├── styles.css                       Estilos da interface
│               └── app.js                           Chamadas REST, estado da aplicação
│
├── apresentacao-frontend/           Placeholder Maven (packaging=pom, sem código Java)
│   └── static/                     Cópia dos mesmos 3 arquivos da SPA
│   └── README.md                   Explica a decisão de duplicação
│
├── bdd/acadtrackbdd/                Módulo de testes BDD
│   └── src/test/
│       ├── java/g8/acadtrack/bdd/
│       │   ├── config/              CucumberTest.java, CucumberSpringConfiguration.java
│       │   ├── steps/               11 classes de step definitions
│       │   ├── support/             LimparBancoDeDadosHook.java, TestContext.java
│       │   └── unit/               ListarRetificacoesUseCaseTest.java
│       └── resources/
│           ├── application.properties   H2 em memória (isolado dos testes)
│           └── features/
│               ├── gestao_disciplina.feature
│               ├── vincular_responsavel.feature
│               ├── lancar_nota.feature
│               ├── analise_desempenho.feature
│               ├── criar_simulado.feature
│               ├── solicitar_retificacao_nota.feature
│               ├── cadastro_email.feature
│               ├── desempenho_reprovado.feature
│               ├── excluir_responsavel.feature
│               ├── notificacao_risco_academico.feature
│               ├── retificacao_guards.feature
│               └── extra/
│                   ├── calcular_media_ponderada.feature
│                   ├── gerar_ranking.feature
│                   └── vincular_aluno_turma.feature
│
├── data/                            Banco H2 em arquivo (gerado automaticamente na 1ª execução)
│                                    Não commitar — está no .gitignore
│
├── dominio-academico/               Domínio: Aluno, Turma, Disciplina
│   └── src/main/java/g8/acadtrack/dominioacademico/
│       ├── aluno/    Aluno.java, AlunoRepository.java, PermissaoResponsavel.java, SituacaoAcademica.java
│       ├── disciplina/ Disciplina.java, DisciplinaRepository.java, StatusDisciplina.java
│       └── turma/    Turma.java, TurmaRepository.java
│
├── dominio-avaliacao/               Domínio: Nota, Simulado, Retificação
│   └── src/main/java/g8/acadtrack/dominioavaliacao/
│       ├── nota/       Nota.java, NotaRepository.java
│       ├── retificacao/ SolicitacaoRetificacao.java, SolicitacaoRetificacaoRepository.java,
│       │                StatusSolicitacaoRetificacao.java (enum: PENDENTE, EM_ANALISE, APROVADA, REPROVADA)
│       └── simulado/   Simulado.java, SimuladoDisciplina.java, SimuladoRepository.java,
│                        SimuladoDisciplinaRepository.java
│
├── dominio-compartilhado/           Artefatos transversais
│   └── src/main/java/g8/acadtrack/dominiocompartilhado/
│       ├── email/      Email.java (Value Object com validação de formato)
│       ├── excecao/    EntidadeNaoEncontradaException, RegraDeNegocioException,
│       │               ConflitoDeEstadoException, AcessoDenegadoException
│       └── risco/      NivelRiscoAcademico.java (enum: BAIXO, MODERADO, ALTO)
│
├── dominio-usuarios/                Domínio: Responsável, Notificação
│   └── src/main/java/g8/acadtrack/dominiousuarios/
│       ├── notificacao/ NotificacaoResponsavel.java, NotificacaoResponsavelRepository.java,
│       │                PrioridadeNotificacao.java, StatusNotificacao.java
│       └── responsavel/ Responsavel.java, ResponsavelRepository.java
│
├── infraestrutura/                  Implementações de persistência (JPA)
│   └── src/main/java/g8/acadtrack/infraestrutura/persistencia/
│       ├── entidade/    9 classes @Entity (uma por agregado):
│       │                AlunoJpaEntity, DisciplinaJpaEntity, NotaJpaEntity,
│       │                NotificacaoResponsavelJpaEntity, ResponsavelJpaEntity,
│       │                SimuladoJpaEntity, SimuladoDisciplinaJpaEntity,
│       │                SolicitacaoRetificacaoJpaEntity, TurmaJpaEntity
│       ├── repositorio/ 9 adaptadores RepositoryJpa — implementam interfaces do domínio,
│       │                delegam para Spring Data, convertem Entity ↔ domínio
│       └── springdata/  9 interfaces JpaRepository (Spring Data)
│
├── scripts/                         Scripts PowerShell utilitários
│   ├── run-backend.ps1              Sobe o backend na primeira porta livre (8080–8299)
│   ├── demo-fluxo-api.ps1          Executa fluxo completo via API já em execução
│   ├── free-ports-if-needed.ps1    Mata processos Java nas portas 8080 e 9001
│   └── README.md                   Descrição de cada script
│
└── docs/                            Documentação acadêmica (ver docs/README.md)
    ├── cml/acadtrack.cml            Modelo Context Mapper
    ├── img/                         Capturas de tela da interface (9 PNGs)
    ├── validacoes/                  Prints de validações via Swagger/API
    └── *.md                         Artefatos DDD, BDD, padrões, etc.
```

---

## Como rodar

### Pré-requisitos

| Requisito | Versão mínima | Observação |
|---|---|---|
| JDK | 17 | Validado também com JDK 25 |
| Maven | 3.8+ | Ou use o wrapper `mvnw.cmd` / `mvnw` incluso |

Não é necessário instalar banco de dados: o H2 é embutido e o arquivo de banco é criado automaticamente em `data/acadtrack-db.mv.db` na primeira execução.

### Subir o backend (forma recomendada no Windows)

```powershell
# Na raiz do projeto (onde fica pom.xml)
.\mvnw.cmd -pl apresentacao-backend -am spring-boot:run
```

Após a mensagem `Started AcadTrackApplication`, acesse:

| URL | O que é |
|---|---|
| `http://localhost:8080/` | Interface web (SPA) |
| `http://localhost:8080/swagger-ui/index.html` | Documentação interativa da API |
| `http://localhost:8080/h2-console` | Console H2 (JDBC URL: `jdbc:h2:file:./data/acadtrack-db`) |

### Linux / macOS / Git Bash

```bash
./mvnw -pl apresentacao-backend -am spring-boot:run
```

### Porta alternativa (se 8080 estiver ocupada)

```powershell
.\mvnw.cmd -pl apresentacao-backend -am spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

### Script com porta automática

O script `scripts/run-backend.ps1` escolhe automaticamente a primeira porta livre entre 8080 e 8299 e imprime o URL do Swagger no terminal:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run-backend.ps1
```

### Liberar porta travada

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\free-ports-if-needed.ps1
```

### Build completo de todos os módulos

```powershell
.\mvnw.cmd clean install
```

---

## Endpoints da API

> A forma mais cômoda de explorar e testar a API é o **Swagger UI** em `http://localhost:8080/swagger-ui/index.html`. Os exemplos abaixo refletem exatamente o código dos controllers.

### Ordem de dependência para o fluxo completo

Ao testar manualmente, crie os recursos nesta ordem, pois uns dependem dos outros:

```
Turmas → Disciplinas → Alunos → Responsáveis
  → Vincular Aluno à Turma → Vincular Responsável ao Aluno
  → Simulados (disciplinas devem existir) → Notas → Retificações
```

---

### Turmas — `GET/POST /turmas`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/turmas` | Lista todas as turmas | 200 |
| POST | `/turmas` | Cria uma turma | 201, 400, 409 |

> Na inicialização, `DadosIniciaisConfig` já cria automaticamente as turmas `1ºA`, `1ºB`, `2ºA`, `2ºB`, `3ºA` e `3ºB`.

**POST** `/turmas`
```json
// request
{ "nome": "Turma Demo" }

// response 201
{ "id": 7, "nome": "Turma Demo" }
```

---

### Alunos — `/alunos`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/alunos` | Lista todos os alunos | 200 |
| GET | `/alunos/{alunoId}` | Busca aluno por ID | 200, 404 |
| POST | `/alunos` | Cadastra aluno | 201, 400, 409 |
| PATCH | `/alunos/{alunoId}` | Edita nome e/ou e-mail | 200, 400, 404, 409 |
| PATCH | `/alunos/{alunoId}/inativar` | Inativa aluno (soft) | 200, 404 |
| PATCH | `/alunos/{alunoId}/ativar` | Reativa aluno | 200, 404 |
| PUT | `/alunos/{alunoId}/turma` | Define ou troca a turma | 200, 400, 404 |
| PUT | `/alunos/{alunoId}/responsavel` | Vincula responsável com permissões | 200, 400, 404, 409 |
| DELETE | `/alunos/{alunoId}/responsavel` | Desvincula responsável | 200, 404 |
| GET | `/alunos/{alunoId}/desempenho` | Análise consolidada de desempenho | 200, 400, 404 |

**POST** `/alunos`
```json
// request — e-mail é único (case-insensitive)
{ "nome": "João Silva", "email": "joao.silva@escola.edu" }

// response 201
{ "id": 1, "nome": "João Silva", "email": "joao.silva@escola.edu",
  "ativo": true, "mediaGlobal": null, "situacaoAcademica": null, "turmaId": null }
```

**PUT** `/alunos/{alunoId}/responsavel`
```json
// request — pelo menos uma permissão deve ser true
{ "responsavelId": 1, "podeVisualizarNotas": true,
  "podeVisualizarSimulados": true, "podeVisualizarDesempenho": false }
```

**GET** `/alunos/{alunoId}/desempenho` — response 200
```json
{
  "alunoId": 1, "nomeAluno": "João Silva",
  "mediaGlobal": 7.25, "situacaoAcademica": "APROVADO",
  "nivelRisco": "BAIXO",
  "historicoSimulados": [
    { "simuladoId": 1, "descricao": "Simulado 1", "mediaPonderada": 7.25,
      "notas": [{ "disciplinaId": 1, "nomeDisciplina": "Matemática", "valor": 7.5 }] }
  ]
}
```

---

### Disciplinas — `/disciplinas`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/disciplinas` | Lista todas | 200 |
| GET | `/disciplinas/{disciplinaId}` | Busca por ID | 200, 404 |
| POST | `/disciplinas` | Cadastra disciplina | 201, 400, 409 |
| PATCH | `/disciplinas/{disciplinaId}` | Edita nome | 200, 400, 404, 409 |
| PATCH | `/disciplinas/{disciplinaId}/inativar` | Inativa (soft-delete) | 200, 400, 404 |
| PATCH | `/disciplinas/{disciplinaId}/ativar` | Reativa | 200, 400, 404 |
| DELETE | `/disciplinas/{disciplinaId}` | Exclui definitivamente | 204, 400, 404 |

**POST** `/disciplinas`
```json
// request
{ "nome": "Matemática" }

// response 201
{ "id": 1, "nome": "Matemática", "ativa": true }
```

---

### Simulados — `/simulados`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/simulados` | Lista (resumo: id + descrição) | 200 |
| GET | `/simulados/{simuladoId}` | Detalha: composição, notas, alunos | 200, 404 |
| GET | `/simulados/{simuladoId}/disciplinas` | Disciplinas vinculadas | 200, 404 |
| POST | `/simulados` | Cria simulado com composição | 201, 400, 404, 409 |
| PATCH | `/simulados/{simuladoId}` | Edita descrição e/ou disciplinas | 200, 400, 404 |

**POST** `/simulados`
```json
// request — mínimo 2 disciplinas distintas e ativas
{ "descricao": "Simulado 1 - Bimestre", "disciplinasIds": [1, 2] }

// response 201
{ "id": 1, "descricao": "Simulado 1 - Bimestre" }
```

---

### Notas — `/notas`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| POST | `/notas` | Lança nota (valida + persiste + recalcula) | 201, 400, 404, 409 |
| GET | `/notas/aluno/{alunoId}` | Lista notas do aluno enriquecidas | 200, 404 |
| GET | `/notas/aluno/{alunoId}/simulado/{simuladoId}/media` | Média ponderada por simulado | 200, 400, 404 |

**POST** `/notas`
```json
// request — disciplina deve estar na composição do simulado
{ "alunoId": 1, "simuladoId": 1, "disciplinaId": 1, "valor": 7.5 }

// response 201
{ "id": 1, "alunoId": 1, "simuladoId": 1, "disciplinaId": 1, "valor": 7.5 }
```

**GET** `/notas/aluno/{alunoId}` — response 200 (enriquecido com nomes)
```json
[
  { "id": 1, "alunoId": 1, "simuladoId": 1, "nomeDisciplina": "Matemática",
    "descricaoSimulado": "Simulado 1 - Bimestre", "disciplinaId": 1, "valor": 7.5 }
]
```

**GET** `/notas/aluno/1/simulado/1/media` — response 200
```json
7.5
```

---

### Rankings — `/rankings`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/rankings?limite=10&criterio=MEDIA_DESC` | Ranking acadêmico geral | 200 |
| GET | `/rankings/{simuladoId}` | Ranking de alunos por simulado | 200, 400, 404 |

**GET** `/rankings?limite=5&criterio=MEDIA_DESC`
```json
[
  { "posicao": 1, "alunoId": 2, "nomeAluno": "Maria", "media": 9.0, "nivelRisco": "BAIXO" },
  { "posicao": 2, "alunoId": 1, "nomeAluno": "João", "media": 7.5, "nivelRisco": "BAIXO" }
]
```

---

### Responsáveis — `/responsaveis`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/responsaveis` | Lista todos | 200 |
| POST | `/responsaveis` | Cadastra responsável | 201, 400, 409 |
| DELETE | `/responsaveis/{responsavelId}` | Exclui e limpa vínculos | 204, 404 |
| GET | `/responsaveis/{responsavelId}/alunos/{alunoId}/notas` | Notas (requer permissão VISUALIZAR_NOTAS) | 200, 403, 404 |
| GET | `/responsaveis/{responsavelId}/alunos/{alunoId}/simulados` | Simulados (requer VISUALIZAR_SIMULADOS) | 200, 403, 404 |
| GET | `/responsaveis/{responsavelId}/alunos/{alunoId}/desempenho` | Desempenho (requer VISUALIZAR_DESEMPENHO) | 200, 400, 403, 404 |
| GET | `/responsaveis/{responsavelId}/notificacoes` | Lista notificações | 200, 404 |
| PATCH | `/responsaveis/{responsavelId}/notificacoes/{notificacaoId}/lida` | Marca notificação como lida | 200, 404 |

**POST** `/responsaveis`
```json
// request — e-mail único no sistema
{ "nome": "Maria Santos", "email": "maria.santos@email.com" }

// response 201
{ "id": 1, "nome": "Maria Santos", "email": "maria.santos@email.com" }
```

---

### Retificações — `/retificacoes`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/retificacoes` | Lista todas (enriquecido) | 200 |
| GET | `/retificacoes/{solicitacaoId}` | Detalha | 200, 404 |
| POST | `/retificacoes` | Solicita retificação | 201, 400, 404, 409 |
| PATCH | `/retificacoes/{solicitacaoId}/em-analise` | Transição PENDENTE → EM_ANALISE | 200, 404, 409 |
| PATCH | `/retificacoes/{solicitacaoId}/aprovar` | Transição EM_ANALISE → APROVADA | 200, 400, 404, 409 |
| PATCH | `/retificacoes/{solicitacaoId}/reprovar` | Transição EM_ANALISE → REPROVADA | 200, 400, 404, 409 |

**POST** `/retificacoes`
```json
// request — justificativa obrigatória
{ "notaId": 1, "justificativa": "Houve erro na correção da questão discursiva" }

// response 201
{ "id": 1, "notaId": 1, "justificativa": "...", "status": "PENDENTE",
  "nomeAluno": "João Silva", "nomeDisciplina": "Matemática", "descricaoSimulado": "Simulado 1" }
```

**PATCH** `/retificacoes/{id}/aprovar`
```json
// request — nota deve estar em EM_ANALISE; justificativa obrigatória
{ "novoValorNota": 9.0, "justificativaDecisao": "Erro confirmado na correção" }
```

---

## Regras de negócio principais

### Aluno
- E-mail é único no sistema (comparação case-insensitive); duplicata retorna 409.
- Aluno inativo não pode receber novas notas, mas pode solicitar retificação de notas já existentes.
- Ao lançar uma nota ou aprovar uma retificação, a **média global simples** (média aritmética de *todas* as notas do aluno, sem peso) é recalculada e a `SituacaoAcademica` é atualizada:
  - `>= 7.0` → APROVADO
  - `>= 5.0 e < 7.0` → RECUPERACAO
  - `< 5.0` → REPROVADO

### Disciplina
- Nome é único (comparação normalizada); duplicata retorna 409.
- Disciplina inativa não pode receber novos lançamentos de nota.
- `PATCH /{id}/inativar` é soft-delete: o registro permanece no banco.
- `DELETE /{id}` é exclusão definitiva.

### Simulado
- Descrição é única (normalizada); duplicata retorna 409.
- Exige pelo menos **2 disciplinas distintas** e **ativas** na composição.
- Não é permitido repetir a mesma disciplina na mesma composição.
- Peso padrão de cada disciplina na composição: `1.0` (definido internamente; não configurável pela API).
- **Média por simulado**: média das notas do aluno *restrita* às disciplinas da composição daquele simulado. Usada em ranking e na análise de desempenho por simulado. Diferente da média global simples.

### Nota
- Valor deve estar entre **0** e **10** (inclusive).
- Um aluno não pode ter duas notas para o mesmo par *(simulado, disciplina)*; duplicata retorna 409.
- A disciplina lançada deve pertencer à composição do simulado.

### Retificação
- Estados: `PENDENTE → EM_ANALISE → APROVADA` ou `→ REPROVADA`.
- Transições inválidas (ex.: aprovar uma solicitação PENDENTE, ou reabrir uma APROVADA) retornam 409.
- Justificativa da solicitação é obrigatória (400 se ausente).
- Justificativa da decisão (aprovação ou reprovação) também é obrigatória.
- Não pode haver duas solicitações abertas (PENDENTE ou EM_ANALISE) para a mesma nota.
- Aprovação atualiza o valor da nota e dispara recálculo de média global e situação do aluno.

### Responsável e permissões
- Responsável só pode consultar dados de alunos com os quais tem vínculo ativo.
- O vínculo carrega três permissões independentes: `VISUALIZAR_NOTAS`, `VISUALIZAR_SIMULADOS`, `VISUALIZAR_DESEMPENHO`.
- Ao vincular, pelo menos uma permissão deve ser `true`; duplicata de vínculo ativo retorna 409.
- Excluir o responsável remove automaticamente vínculos existentes.

### Risco acadêmico e notificações
- Após lançamento de nota ou aprovação de retificação, o sistema classifica o risco:
  - Média `< 5.0` → ALTO
  - `>= 5.0 e < 7.0` → MODERADO
  - `>= 7.0` → BAIXO
- Se o risco for MODERADO ou ALTO e o aluno tiver responsável vinculado, uma `NotificacaoResponsavel` é criada automaticamente com a prioridade correspondente.
- O responsável pode listar suas notificações e marcá-las como lidas.

---

## Padrões de projeto

Cinco padrões foram implementados como parte da Entrega 2, cada um mapeado a uma necessidade real do domínio:

| Padrão | Onde está no código | Por que foi usado |
|---|---|---|
| **Template Method** | `FluxoAnaliseAcademicaTemplate` (`aplicacao/nota/`) | Define as 4 etapas fixas da análise acadêmica (coleta → consolidação → classificação → notificação); subclasses especializadas podem customizar etapas sem quebrar a sequência |
| **Decorator** | `ValidadorLancamentoNotaDecorator` + 5 decoradores concretos (`aplicacao/nota/validacao/`) | Encadeia validações de nota em ordem sem acoplar `LancarNotaUseCase` a cada regra individual; adicionar uma nova validação não exige alterar o use case |
| **Domain Events** | `DomainEvent`, `RiscoAcademicoEvent`, `DomainEventPublisher`, `SpringDomainEventPublisher`, `NotificarResponsavelRiscoAcademicoHandler` | Desacopla o domínio e os casos de uso da geração de notificações; novos publicadores assíncronos podem ser adicionados sem alterar os agregados |
| **Proxy** | `AcessoResponsavelAlunoProxy` (`aplicacao/responsavel/`) | Intercepta qualquer consulta de responsável a dados de aluno e verifica vínculo ativo + permissão antes de delegar; centraliza o controle de acesso num único ponto |
| **Strategy** | `EstrategiaClassificacaoRiscoAcademico` + `RiscoAltoStrategy`, `RiscoBaixoStrategy`, `RiscoModeradoStrategy` (`aplicacao/nota/risco/`) | Permite trocar o critério de classificação de risco (ex.: por frequência, por média combinada) sem alterar o fluxo de análise |

---

## Como rodar os testes

### Todos os módulos (inclui BDD)

```powershell
.\mvnw.cmd test
```

### Apenas o módulo BDD

```powershell
.\mvnw.cmd -pl bdd/acadtrackbdd test
```

Os testes BDD usam H2 em memória (configurado em `bdd/acadtrackbdd/src/test/resources/application.properties`), isolado do banco de desenvolvimento em `data/`. O hook `LimparBancoDeDadosHook` limpa as tabelas antes de cada cenário.

**Resultado esperado:**
```
Tests run: 77, Failures: 0, Errors: 0, Skipped: 0 — BUILD SUCCESS
```

### Teste unitário

`ListarRetificacoesUseCaseTest` em `bdd/acadtrackbdd/src/test/java/.../unit/` é um teste JUnit puro do use case de listagem de retificações.

### Features BDD (Gherkin)

| Arquivo | Funcionalidade coberta | Cenários |
|---|---|---|
| `gestao_disciplina.feature` | F1 — Gestão de disciplinas | Criar, inativar, ativar, duplicidade |
| `vincular_responsavel.feature` | F2 — Responsáveis | Vínculo, permissões, desvinculação, acesso negado |
| `lancar_nota.feature` | F3 — Lançamento de notas | Nota válida, inválida, duplicada, recálculo de média e situação |
| `analise_desempenho.feature` | F4 — Análise de desempenho | Histórico consolidado, classificação de risco |
| `criar_simulado.feature` | F5 — Simulados | Composição válida, restrições de disciplinas |
| `solicitar_retificacao_nota.feature` | F6 — Retificação | 16 cenários do fluxo completo de estados |
| `notificacao_risco_academico.feature` | Observer / notificações | Criação e leitura de notificações |
| `retificacao_guards.feature` | Guardas de estado | Transições inválidas bloqueadas |
| `cadastro_email.feature` | Validação de e-mail | Formato inválido, duplicidade |
| `desempenho_reprovado.feature` | Situação acadêmica | Cenários de reprovação |
| `excluir_responsavel.feature` | Exclusão de responsável | Remoção com limpeza de vínculo |
| `extra/calcular_media_ponderada.feature` | Média por simulado | Cálculo ponderado |
| `extra/gerar_ranking.feature` | Ranking | Ordenação por média |
| `extra/vincular_aluno_turma.feature` | Vínculo aluno-turma | Turma inexistente, troca de turma |

---

## Decisões arquiteturais

### Por que Clean Architecture com módulos Maven separados?

O domínio (`dominio-*`) não tem nenhuma anotação Spring ou JPA. Isso garante que regras de negócio podem ser testadas sem subir contexto Spring e que uma troca de framework (ex.: Quarkus) afetaria apenas `infraestrutura/` e `apresentacao-backend/`, não o domínio.

### Por que H2 em arquivo e não PostgreSQL?

H2 elimina pré-requisitos de instalação para rodar o projeto. O uso de `spring.jpa.hibernate.ddl-auto=update` faz o schema ser gerenciado automaticamente. A troca para PostgreSQL exigiria apenas alterar `application.properties` e o driver — os repositórios JPA não mudam.

### Por que o frontend está em dois lugares?

`apresentacao-frontend/` existe como módulo Maven para representar conceitualmente a camada de apresentação web dentro da arquitetura multi-módulo. Os arquivos reais (`index.html`, `styles.css`, `app.js`) ficam em `apresentacao-backend/src/main/resources/static/` porque é de lá que o Tomcat embutido do Spring Boot serve arquivos estáticos automaticamente. A duplicação é intencional e documentada em `apresentacao-frontend/README.md`.

### Por que não há autenticação?

Nesta entrega, o controle de acesso é feito no nível de regra de negócio (Proxy de responsável) e não por sessão/token HTTP. O `Coordenador` é uma persona de negócio documentada, sem role técnica implementada. Autenticação (Spring Security + JWT, por exemplo) é evolução prevista para entregas futuras.

### Por que os scripts ficam em `scripts/` e não na raiz?

Convenção comum em projetos multi-módulo Maven: scripts de desenvolvimento e demo não pertencem ao código de produção e ficam numa pasta dedicada. Isso mantém a raiz limpa e os caminhos estáveis para referência no README.

### Por que a média é calculada de duas formas diferentes?

Duas métricas distintas coexistem intencionalmente:

- **Média global simples**: média aritmética de *todas* as notas do aluno (sem peso, sem filtro por simulado). É a base para a `SituacaoAcademica` persistida no cadastro do aluno (APROVADO / RECUPERACAO / REPROVADO). Recalculada a cada lançamento ou aprovação de retificação.
- **Média por simulado**: média das notas do aluno *restrita* às disciplinas da composição de um simulado específico, com peso padrão `1.0`. Usada para ranking por simulado e na análise de desempenho histórico por avaliação.

A separação reflete uma distinção pedagógica real: a situação geral do aluno baseia-se em toda sua trajetória, enquanto o ranking de um simulado reflete apenas aquela avaliação.

---

## Glossário do domínio

Termos usados de forma consistente no código, nos testes e na documentação:

| Termo | Definição | Onde aparece no código |
|---|---|---|
| **Aluno** | Participante que realiza simulados e acumula notas | `Aluno.java`, `AlunoJpaEntity` |
| **Turma** | Agrupamento de alunos; um aluno pertence a no máximo uma turma | `Turma.java` |
| **Disciplina** | Componente avaliado dentro de um simulado (ex.: Matemática) | `Disciplina.java` |
| **Simulado** | Avaliação composta por pelo menos duas disciplinas distintas e ativas | `Simulado.java` |
| **SimuladoDisciplina** | Associação entre simulado e disciplina com peso padrão interno | `SimuladoDisciplina.java` |
| **Nota** | Resultado do aluno num par (simulado, disciplina); valor entre 0 e 10 | `Nota.java` |
| **Média global simples** | Média aritmética de *todas* as notas do aluno; base para SituacaoAcademica | `AvaliacaoAcademicaService` |
| **Média por simulado** | Média das notas do aluno restrita às disciplinas de um simulado; usada em rankings | `CalcularMediaPonderadaUseCase` |
| **SituacaoAcademica** | Estado do aluno (APROVADO / RECUPERACAO / REPROVADO); persistido no cadastro | `SituacaoAcademica.java` |
| **NivelRiscoAcademico** | Classificação de risco (BAIXO / MODERADO / ALTO) calculada pela média global | `NivelRiscoAcademico.java` |
| **SolicitacaoRetificacao** | Pedido de revisão de uma nota; percorre estados PENDENTE → EM_ANALISE → APROVADA/REPROVADA | `SolicitacaoRetificacao.java` |
| **StatusSolicitacaoRetificacao** | Enum com os 4 estados possíveis da retificação | `StatusSolicitacaoRetificacao.java` |
| **Responsavel** | Usuário (pai/responsável legal) com acesso controlado a dados de alunos vinculados | `Responsavel.java` |
| **PermissaoResponsavel** | Enum com as permissões granulares: `VISUALIZAR_NOTAS`, `VISUALIZAR_SIMULADOS`, `VISUALIZAR_DESEMPENHO` | `PermissaoResponsavel.java` |
| **NotificacaoResponsavel** | Alerta criado automaticamente quando o aluno entra em risco MODERADO ou ALTO | `NotificacaoResponsavel.java` |
| **Coordenador** | Persona de negócio responsável pela gestão acadêmica; não implementado como role técnica de autenticação nesta entrega | Documentação, cenários BDD |
| **Professor** | Persona de negócio responsável pelo lançamento e análise de notas; não implementado como role técnica | Documentação, cenários BDD |

---

## Documentação acadêmica

| Artefato | Localização |
|---|---|
| Descrição do domínio + Linguagem Onipresente | [`docs/descricao_do_dominio.md`](docs/descricao_do_dominio.md) |
| Mapa de histórias (Story Map) | [`docs/story_map_personas.md`](docs/story_map_personas.md), [`docs/story_map.pdf`](docs/story_map.pdf) |
| Protótipos (Figma + capturas) | [`docs/prototipos.md`](docs/prototipos.md) |
| Modelo CML (Context Mapper) | [`docs/cml/acadtrack.cml`](docs/cml/acadtrack.cml) |
| Bounded Contexts (resumo) | [`docs/cml/bounded_contexts.md`](docs/cml/bounded_contexts.md) |
| DDD nos 4 níveis | [`docs/ddd_niveis.md`](docs/ddd_niveis.md) |
| Arquitetura Limpa | [`docs/arquitetura_limpa.md`](docs/arquitetura_limpa.md) |
| Padrões de projeto (Entrega 2) | [`docs/padroes_entrega2.md`](docs/padroes_entrega2.md) |
| Persistência ORM/JPA | [`docs/persistencia_orm_entrega2.md`](docs/persistencia_orm_entrega2.md) |
| Cenários BDD | [`bdd/acadtrackbdd/src/test/resources/features/`](bdd/acadtrackbdd/src/test/resources/features/) |
| Roteiro de demonstração | [`docs/script_demonstracao.md`](docs/script_demonstracao.md) |
| Guia Swagger passo a passo | [`docs/demo_fluxo_swagger_passo_a_passo.md`](docs/demo_fluxo_swagger_passo_a_passo.md) |
| Validações com prints | [`docs/validacoes.md`](docs/validacoes.md) |

Links externos:
- **Protótipo Figma**: [https://stew-skip-70401626.figma.site](https://stew-skip-70401626.figma.site)
- **Story Map (Avion)**: [https://sistema-acadtrack.avion.io/share/8rNKdtSMQmCNdr3u3](https://sistema-acadtrack.avion.io/share/8rNKdtSMQmCNdr3u3)
- **Slides (Gamma)**: [https://gamma.app/docs/AcadTrack-fbl5e19j5zy2rvi](https://gamma.app/docs/AcadTrack-fbl5e19j5zy2rvi)
- **Screencast**: [https://www.loom.com/share/33626c63524f4091921125d26fdfe3aa](https://www.loom.com/share/33626c63524f4091921125d26fdfe3aa)

---

## Membros

| Nome | E-mail | Funcionalidades / Padrões |
|---|---|---|
| **Erick Belo** | eab2@cesar.school | F3 (Lançamento de notas), F6 (Retificação); Decorator, Observer |
| **João Marcelo Montenegro** | jmtpm@cesar.school | F1 (Disciplinas), F2 (Responsáveis); Proxy |
| **Rodrigo Torres** | rtgf@cesar.school | F4 (Análise de desempenho), F5 (Simulados); Template Method, Strategy |
