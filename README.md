# AcadTrack — Plataforma de Gestão Acadêmica

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-007396?logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.14-6DB33F?logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-3.8%2B-C71A36?logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/Cucumber-BDD-23D96C?logo=cucumber&logoColor=white" />
  <img src="https://img.shields.io/badge/JPA%2FHibernate-59666C?logo=hibernate&logoColor=white" />
  <img src="https://img.shields.io/badge/H2-Database-003545" />
</p>

Sistema web para gestão de simulados acadêmicos e acompanhamento do desempenho de alunos. Coordenadores organizam avaliações, professores lançam notas, alunos acompanham seu histórico e responsáveis consultam resultados — com regras de negócio não triviais, validações orientadas ao domínio e arquitetura limpa em módulos Maven separados.

---

## Sumário

**Parte 1 — 1ª Entrega**
- [Domínio e linguagem onipresente](#1-domínio-e-linguagem-onipresente)
- [Mapa de histórias do usuário](#2-mapa-de-histórias-do-usuário)
- [Protótipos](#3-protótipos)
- [Modelo DDD — Context Mapper](#4-modelo-ddd--context-mapper)
- [DDD nos 4 níveis](#5-ddd-nos-4-níveis)
- [Cenários BDD e testes Cucumber](#6-cenários-bdd-e-testes-cucumber)
- [Arquitetura Limpa](#7-arquitetura-limpa)

**Parte 2 — 2ª Entrega**
- [Padrões de projeto](#8-padrões-de-projeto)
- [Camada de persistência](#9-camada-de-persistência)
- [Camada de apresentação web](#10-camada-de-apresentação-web)
- [Como rodar o projeto](#11-como-rodar-o-projeto)

---

---

# PARTE 1 — 1ª Entrega ("COMMIT FINAL DA ENTREGA 1" - 29 de abril de 2026)

---

## 1. Domínio e linguagem onipresente

### O que o sistema resolve

Instituições de ensino precisam acompanhar o desempenho de alunos ao longo de múltiplas avaliações com diferentes disciplinas e critérios de aprovação. O fluxo manual é sujeito a erros: notas incorretas, responsáveis sem acesso organizado e sem alertas quando um aluno entra em risco.

O AcadTrack organiza esse processo em torno de cinco fluxos principais:

1. **Criação de simulados** com composição validada (mínimo duas disciplinas distintas e ativas).
2. **Lançamento de notas** com recálculo automático da média e da situação acadêmica do aluno.
3. **Fluxo de retificação** com máquina de estados (PENDENTE → EM_ANALISE → APROVADA / REPROVADA).
4. **Análise de risco** com classificação automática (BAIXO / MODERADO / ALTO) e notificação do responsável.
5. **Portal do responsável** com acesso controlado por permissões granulares.


---

### Subdomínios

| Subdomínio | Tipo | Responsabilidade |
|---|---|---|
| **Gestão Acadêmica** | Core Domain | Alunos, turmas, simulados, ranking, análise de desempenho |
| **Avaliação** | Supporting Domain | Notas, cálculo de médias, retificações |
| **Usuários** | Generic Domain | Responsáveis, vínculos, notificações, controle de acesso |

### Linguagem onipresente

Os termos abaixo são usados de forma consistente no código (nomes de classes), nos cenários BDD (texto Gherkin) e na documentação:

| Termo | Definição no domínio |
|---|---|
| **Aluno** | Participante que realiza simulados; possui situação acadêmica e pode ter um responsável vinculado |
| **Turma** | Agrupamento de alunos; um aluno pertence a no máximo uma turma por vez |
| **Disciplina** | Componente curricular avaliado dentro de um simulado (ex.: Matemática); pode estar ATIVA ou INATIVA |
| **Simulado** | Avaliação composta por pelo menos duas disciplinas distintas e ativas, cada uma com peso padrão interno |
| **SimuladoDisciplina** | Associação entre um Simulado e uma Disciplina com peso padrão `1.0` |
| **Nota** | Resultado de um aluno para um par *(simulado, disciplina)*; valor entre 0 e 10 |
| **Média global simples** | Aritmética de *todas* as notas do aluno, sem peso; base para atualizar a `SituacaoAcademica` persistida no cadastro |
| **Média por simulado** | Média das notas do aluno restrita às disciplinas da composição de um simulado específico; usada em rankings e análise histórica |
| **SituacaoAcademica** | Estado calculado a partir da média global: `APROVADO` (≥ 7.0) / `RECUPERACAO` (≥ 5.0) / `REPROVADO` (< 5.0) |
| **NivelRiscoAcademico** | Classificação de risco calculada pela Strategy: `ALTO` / `MODERADO` / `BAIXO` |
| **SolicitacaoRetificacao** | Pedido de revisão de nota com estados PENDENTE → EM_ANALISE → APROVADA / REPROVADA |
| **Responsavel** | Responsável legal do aluno; acessa dados do aluno mediante permissões explícitas |
| **PermissaoResponsavel** | Enum com três permissões independentes: `VISUALIZAR_NOTAS`, `VISUALIZAR_SIMULADOS`, `VISUALIZAR_DESEMPENHO` |
| **NotificacaoResponsavel** | Alerta criado automaticamente quando o aluno entra em risco MODERADO ou ALTO |
| **Coordenador / Professor** | Personas de negócio documentadas no story map e nos cenários BDD; não têm role técnica de autenticação nesta entrega |

> Documentação completa: [`docs/descricao_do_dominio.md`](docs/descricao_do_dominio.md)

---

## 2. Mapa de histórias do usuário

O story map organiza as funcionalidades por persona e release, deixando claro o que está implementado e o que é evolução futura.

### Personas

| Persona | Foco no sistema |
|---|---|
| **Coordenador** | Gerencia disciplinas, turmas e simulados; organiza o ambiente acadêmico |
| **Professor** | Lança e revisa notas; conduz o processo avaliativo |
| **Aluno** | Acompanha desempenho e solicita retificação de notas |
| **Responsável** | Consulta notas, simulados e desempenho do aluno vinculado com permissões |

### Story Map resumido (Release 1 — implementado)

| Persona | Atividade | Funcionalidades entregues |
|---|---|---|
| Coordenador | Gerenciar disciplinas | Cadastrar, inativar/ativar, bloquear duplicata normalizada |
| Coordenador | Gerenciar simulados | Criar com composição válida (min 2 disciplinas ativas, sem repetidas), bloquear alteração com notas lançadas |
| Coordenador | Organizar turmas | Vincular aluno a turma, trocar turma, limpar duplicatas |
| Professor | Lançar e validar notas | Lançar nota (0–10), bloquear duplicidade, atualizar média e situação automaticamente |
| Professor | Tratar retificações | Iniciar análise, aprovar (atualiza nota e situação) ou reprovar com justificativa |
| Aluno | Solicitar retificação | Abrir solicitação com justificativa; impedir múltiplas em aberto para a mesma nota |
| Responsável | Consultar informações | Notas, simulados e desempenho com vínculo e permissões validados pelo Proxy |

> Artefatos: [`docs/story_map_personas.md`](docs/story_map_personas.md) · [`docs/story_map.pdf`](docs/story_map.pdf)

---

## 3. Protótipos

O protótipo foi desenvolvido em **alta fidelidade no Figma** e cobre todas as telas implementadas. A interface final segue fielmente o protótipo.

### Screencast

[Assistir screencast do AcadTrack](https://www.loom.com/share/33626c63524f4091921125d26fdfe3aa)

**Telas disponíveis** (capturas em [`docs/img/`](docs/img/)):

| Tela | Arquivo |
|---|---|
| Dashboard | `tela-dashboard.png` |
| Alunos | `tela-alunos.png` |
| Disciplinas | `tela-disciplinas.png` |
| Notas / Histórico de Notas | `tela-notas.png` |
| Simulados | `tela-simulado.png` |
| Análise de Desempenho | `tela-desempenho.png` |
| Retificação de notas | `tela-retificacao.png` |
| Responsáveis | `tela-responsaveis.png` |
| Portal do responsável | `tela-portal-responsavel.png` |

> Protótipo Figma: [https://stew-skip-70401626.figma.site](https://stew-skip-70401626.figma.site)  
> Documentação: [`docs/prototipos.md`](docs/prototipos.md)

---

## 4. Modelo DDD — Context Mapper

O arquivo **`acadtrack.cml`** na raiz do repositório é o modelo DDD estratégico legível por máquina, compatível com a ferramenta [Context Mapper](https://contextmapper.org/). Ele declara os quatro Bounded Contexts e seus relacionamentos.

```
AcadTrack/
└── acadtrack.cml   ← modelo DDD estratégico (Context Mapper)
```

### Bounded Contexts declarados no CML

| Bounded Context | Implementado no módulo | Conteúdo principal |
|---|---|---|
| `CompartilhadoContext` | `dominio-compartilhado` | `Email` (Value Object), `NivelRiscoAcademico`, `DomainEvent` |
| `GestaoAcademicaContext` | `dominio-academico` | `Aluno`, `Turma`, `Disciplina`, `RiscoAcademicoEvent` |
| `AvaliacaoDesempenhoContext` | `dominio-avaliacao` | `Nota`, `Simulado`, `SimuladoDisciplina`, `SolicitacaoRetificacao` |
| `UsuariosContext` | `dominio-usuarios` | `Responsavel`, `NotificacaoResponsavel` |

### Relacionamentos entre contextos

- `GestaoAcademicaContext` usa `CompartilhadoContext` (Shared Kernel)
- `AvaliacaoDesempenhoContext` usa `CompartilhadoContext` (Shared Kernel)
- `UsuariosContext` usa `GestaoAcademicaContext` para vincular responsável a aluno

> Resumo textual dos contextos: [`docs/cml/bounded_contexts.md`](docs/cml/bounded_contexts.md)

---

## 5. DDD nos 4 níveis

### Nível Preliminar

Problema identificado: gerenciar simulados acadêmicos e acompanhar o desempenho dos alunos de forma estruturada, com lançamento de notas, controle de retificações e acesso organizado para responsáveis.

Conceitos centrais identificados na exploração inicial: *aluno, turma, disciplina, simulado, nota, média, ranking, retificação, responsável*.

### Nível Estratégico

O sistema foi dividido em três subdomínios com responsabilidades distintas (ver seção 1). O arquivo `acadtrack.cml` na raiz codifica o Context Map com os quatro Bounded Contexts e seus relacionamentos.

### Nível Tático

**Entidades** (objetos com identidade própria):

| Entidade | Módulo | Invariantes principais |
|---|---|---|
| `Aluno` | `dominio-academico` | E-mail único; situação acadêmica recalculada a cada lançamento |
| `Turma` | `dominio-academico` | Nome normalizado único |
| `Disciplina` | `dominio-academico` | Nome normalizado único; status ATIVA/INATIVA |
| `Nota` | `dominio-avaliacao` | Valor em [0,10]; par (aluno, simulado, disciplina) único |
| `Simulado` | `dominio-avaliacao` | Mínimo 2 disciplinas distintas e ativas; descrição única normalizada |
| `SolicitacaoRetificacao` | `dominio-avaliacao` | Máquina de estados com guardas de transição |
| `Responsavel` | `dominio-usuarios` | E-mail único |

**Value Objects** (objetos sem identidade, imutáveis):

| Value Object | Módulo | O que representa |
|---|---|---|
| `Email` | `dominio-compartilhado` | Endereço de e-mail normalizado (trim + lowercase) com validação de formato. Campo: `String endereco`. Construtor lança `RegraDeNegocioException` se inválido. Possui `equals()` e `hashCode()` baseados no valor. |
| `SimuladoDisciplina` | `dominio-avaliacao` | Associação simulado–disciplina com peso padrão `1.0` |
| `NivelRiscoAcademico` | `dominio-compartilhado` | Enum: `BAIXO`, `MODERADO`, `ALTO` |
| `SituacaoAcademica` | `dominio-academico` | Enum: `APROVADO`, `RECUPERACAO`, `REPROVADO` |

**Repositórios** (interfaces no domínio, implementadas na infraestrutura):

```
dominio-academico:   AlunoRepository, TurmaRepository, DisciplinaRepository
dominio-avaliacao:   NotaRepository, SimuladoRepository, SimuladoDisciplinaRepository,
                     SolicitacaoRetificacaoRepository
dominio-usuarios:    ResponsavelRepository, NotificacaoResponsavelRepository
```

**Domain Events** (publicados pelo domínio, consumidos por handlers na camada de aplicação):

| Evento | Publicado por | Consumido por |
|---|---|---|
| `RiscoAcademicoEvent` | `Aluno.registrarRiscoAcademicoIdentificado()` | `NotificarResponsavelRiscoAcademicoHandler` |

**Factory** (cria objetos com estado inicial garantido):

| Factory | Cria | Invariante protegida |
|---|---|---|
| `SolicitacaoRetificacaoFabrica.nova(notaId, justificativa)` | `SolicitacaoRetificacao` | Status sempre `PENDENTE`; `justificativaDecisao` sempre `null` na criação |

**Serviços de domínio** (lógica que não pertence a uma entidade específica):

```
AvaliacaoAcademicaService   — calcula média global simples e situação acadêmica
ClassificadorRiscoAcademicoService — classifica risco usando Strategy pattern
```

### Nível Operacional

Implementação completa com tecnologias concretas:

- **Backend**: Spring Boot 3.5.14, Java 17
- **Persistência**: JPA/Hibernate 6, Spring Data, H2 (arquivo), Flyway
- **API**: REST com OpenAPI/Swagger
- **Frontend**: SPA vanilla JS servida como recurso estático pelo Spring Boot
- **Testes**: Cucumber 7.18.1 + JUnit Platform + Spring Test

> Documentação completa: [`docs/ddd_niveis.md`](docs/ddd_niveis.md)

---

## 6. Cenários BDD e testes Cucumber

### Abordagem

Os cenários BDD especificam o comportamento do sistema em linguagem natural (Gherkin, em português) e são executados automaticamente contra a aplicação real — contexto Spring completo com H2 em memória e MockMvc para chamadas HTTP.

### Estrutura do módulo de testes

```
bdd/acadtrackbdd/src/test/
├── java/g8/acadtrack/bdd/
│   ├── CucumberTest.java                    Runner com @Suite @IncludeEngines("cucumber")
│   ├── CucumberSpringConfiguration.java     @CucumberContextConfiguration + @SpringBootTest
│   ├── steps/                               14 classes de step definitions
│   │   ├── GestaoAlunoSteps.java
│   │   ├── GestaoTurmaSteps.java
│   │   ├── GestaoDisciplinaSteps.java
│   │   ├── GestaoSimuladoSteps.java
│   │   ├── LancarNotaSteps.java
│   │   ├── AnalisarDesempenhoSteps.java
│   │   ├── SolicitarRetificacaoSteps.java
│   │   ├── VincularResponsavelSteps.java
│   │   ├── ExcluirResponsavelSteps.java
│   │   ├── NotificacaoRiscoAcademicoSteps.java
│   │   ├── CadastroEmailSteps.java
│   │   ├── CalcularMediaPonderadaSteps.java
│   │   ├── GerarRankingSteps.java
│   │   └── VincularAlunoTurmaSteps.java
│   ├── support/
│   │   └── LimparBancoDeDadosHook.java      Limpa todas as tabelas antes de cada cenário
│   └── unit/                                5 testes JUnit puros (sem Spring)
│       ├── EmailTest.java
│       ├── RiscoAcademicoStrategyTest.java
│       ├── OrdenarRankingAcademicoServiceTest.java
│       ├── AvaliacaoAcademicaServiceTest.java
│       └── ListarRetificacoesUseCaseTest.java
└── resources/
    ├── application.properties               H2 memória, ddl-auto=update, Flyway desabilitado
    └── features/                            14 arquivos .feature
```

### Cenários por funcionalidade

| Feature file | Cenários | O que cobre |
|---|---|---|
| `gestao_disciplina.feature` | 4 | Criar disciplina ATIVA, rejeitar duplicata normalizada, bloquear nota em inativa, bloquear simulado com inativa |
| `vincular_responsavel.feature` | 9 | Vincular responsável, rejeitar duplicata de vínculo, desvincular, bloquear acesso sem vínculo, bloquear por permissão insuficiente (notas, simulados, desempenho) |
| `excluir_responsavel.feature` | 3 | Excluir sem aluno vinculado, excluir e limpar vínculo do aluno, rejeitar ID inexistente |
| `lancar_nota.feature` | 9 | Nota válida, nota via API, nota fora de [0,10], aluno inativo, duplicata, recálculo de média, limiares de situação (5.0 e 7.0) |
| `desempenho_reprovado.feature` | 1 | Confirmar REPROVADO e risco ALTO para média < 5.0 |
| `analise_desempenho.feature` | 6 | Análise sem risco, risco ALTO, risco MODERADO, posição no ranking, aluno sem notas, erro 400 via API |
| `notificacao_risco_academico.feature` | 6 | Notificação para risco ALTO/MODERADO, ausência para BAIXO, sem falha se aluno sem responsável, listar notificações, marcar como lida |
| `criar_simulado.feature` | 8 | Criar com 2+ disciplinas, rejeitar sem disciplinas, com só 1, com repetidas, com inexistentes, descrição duplicada, com notas lançadas, com disciplina inativa |
| `solicitar_retificacao_nota.feature` | 16 | Solicitação válida, sem justificativa, nota inexistente, múltiplas em aberto, PENDENTE → EM_ANALISE, aprovar (atualiza nota e situação), reprovar (nota intacta), guardas de estado, justificativas obrigatórias, aluno inativo pode solicitar, listagem enriquecida |
| `retificacao_guards.feature` | 2 | Bloquear APROVADA → EM_ANALISE, bloquear REPROVADA → EM_ANALISE |
| `cadastro_email.feature` | 5 | Aluno sem e-mail, e-mail inválido, e-mail duplicado por capitalização, responsável sem e-mail, responsável com e-mail inválido |
| `extra/calcular_media_ponderada.feature` | 1 | Média ponderada correta para composição padrão do simulado |
| `extra/gerar_ranking.feature` | 3 | Alunos ordenados por média, ranking vazio sem notas, consistência com análise de desempenho |
| `extra/vincular_aluno_turma.feature` | 4 | Vincular, trocar turma, rejeitar turma duplicada, limpar duplicatas migrando alunos |
| **Total** | **77** | |

### Exemplo de cenário BDD

```gherkin
# features/lancar_nota.feature
Feature: Lançar nota individual

  Scenario: Recalcular média automaticamente após novo lançamento
    Dado que o aluno "João Silva" possui nota 6.0 já lançada
    Quando o professor lança uma nova nota 8.0 para o aluno "João Silva" em outra disciplina
    Então o sistema atualiza a média do aluno para 7.0

  Scenario: Definir situação acadêmica no limite de aprovação (média 7.0)
    Dado que o aluno "João Silva" possui nota 6.0 já lançada
    Quando o professor lança uma nova nota 8.0 para o aluno "João Silva" em outra disciplina
    Então o sistema atualiza a situação acadêmica do aluno para "APROVADO"
```
## API e Swagger

Com o backend em execução, a documentação interativa da API pode ser acessada em:

- **Swagger UI:** <http://localhost:8080/swagger-ui/index.html>
- **OpenAPI (JSON):** <http://localhost:8080/v3/api-docs>

Observações para demonstração e testes:

- É necessário iniciar o backend antes de abrir o Swagger.
- A porta pode ser outra se usar `scripts/run-backend.ps1`, pois o script escolhe uma porta livre e imprime a URL.
- Fluxo automático: com o servidor já rodando, execute:

```powershell
.\scripts\demo-fluxo-api.ps1 -BaseUrl 'http://localhost:PORT'
```


### Como rodar os testes

**Pré-requisitos**: JDK 17+ e Maven 3.8+ (ou use `mvnw.cmd` / `mvnw`).

```powershell
# Suite completa: 77 Cucumber + 24 JUnit = 101 testes
.\mvnw.cmd test

# Apenas o módulo BDD (o -am compila todos os módulos upstream antes)
.\mvnw.cmd test -pl bdd/acadtrackbdd -am
```

```bash
# Linux / macOS
./mvnw test -pl bdd/acadtrackbdd -am
```

**Resultado esperado:**
```
Tests run: 101, Failures: 0, Errors: 0, Skipped: 0 — BUILD SUCCESS
```

> Os testes BDD usam H2 em memória, isolado do banco de desenvolvimento. O Flyway é desabilitado nos testes (`spring.flyway.enabled=false`); o schema é criado pelo `ddl-auto=update` do Hibernate no contexto de teste.

> Documentação completa da cobertura BDD: [`docs/bdd.md`](docs/bdd.md)

---

## 7. Arquitetura Limpa

### Princípio fundamental

As dependências sempre apontam para dentro: o domínio não conhece Spring, JPA nem nenhuma infraestrutura. Uma troca de framework afetaria apenas `infraestrutura/` e `apresentacao-backend/`, não as regras de negócio.

### Diagrama de camadas

```
┌──────────────────────────────────────────────────────────────┐
│                    apresentacao-backend                      │
│  @RestController, DTOs, GlobalExceptionHandler, Swagger      │
│  Único módulo com main() executável                          │
└────────────────────────────┬─────────────────────────────────┘
                             │ chama use cases por injeção
┌────────────────────────────▼─────────────────────────────────┐
│                       aplicacao                              │
│  Use Cases, padrões de projeto, serviços de domínio          │
│  Sem Spring (@Service é a única exceção), sem JPA            │
│  Acessa repositórios por interfaces (nunca por implementação)│
└────────────────┬──────────────────────┬──────────────────────┘
    implementa   │                      │ usa entidades
    interfaces   │                      │ do domínio
┌───────────────▼──────┐  ┌────────────▼──────────────────────┐
│    infraestrutura    │  │        dominio-academico           │
│  @Entity, JPA,       │  │        dominio-avaliacao           │
│  Spring Data,        │  │        dominio-usuarios            │
│  Flyway,             │  │        dominio-compartilhado       │
│  SpringDomainEvent   │  │                                    │
│  Publisher           │  │  Entidades, Value Objects,         │
└──────────────────────┘  │  interfaces de repositório,        │
                          │  Domain Events, Factory            │
                          │  Sem Spring. Sem JPA.              │
                          └────────────────────────────────────┘
         ▲
         │ apresentacao-frontend
         │  SPA (index.html + app.js) servida como
         │  recurso estático via classpath:/static/
```

### Módulos Maven e regra de dependência

| Módulo | Dependências permitidas |
|---|---|
| `dominio-compartilhado` | Nenhuma interna |
| `dominio-academico` | `dominio-compartilhado` |
| `dominio-avaliacao` | `dominio-compartilhado` |
| `dominio-usuarios` | `dominio-compartilhado` |
| `aplicacao` | Todos os `dominio-*` |
| `infraestrutura` | `aplicacao` + todos os `dominio-*` |
| `apresentacao-backend` | `infraestrutura` + `aplicacao` + todos os `dominio-*` |
| `apresentacao-frontend` | Nenhuma Java — apenas recursos estáticos |
| `bdd/acadtrackbdd` | `apresentacao-backend` (transitivamente tudo) |

O compilador Maven impede que qualquer módulo de domínio importe algo de `infraestrutura` ou `aplicacao`. A regra de dependência é verificada em tempo de compilação, não por convenção.

### Por que o frontend está num módulo separado?

`apresentacao-frontend` tem `packaging=jar` e contém os arquivos estáticos da SPA em `src/main/resources/static/`. O `apresentacao-backend` o declara como dependência Maven — assim os arquivos ficam em `classpath:/static/` do jar executável. O Spring Boot serve automaticamente qualquer `classpath:/static/` sem configuração adicional. Essa separação reflete a arquitetura em módulos: frontend e backend têm ciclos de vida independentes.

> Documentação detalhada: [`docs/arquitetura_limpa.md`](docs/arquitetura_limpa.md)

---

---

# PARTE 2 — 2ª Entrega

---

## 8. Padrões de projeto

Sete padrões GoF implementados, cada um mapeado a uma necessidade real do domínio. Nenhum foi adicionado artificialmente.

---

### Factory

**Intenção:** centralizar a criação de objetos com estado inicial complexo, garantindo que invariantes de domínio sejam respeitados desde a criação — sem depender do chamador para saber os valores corretos.

**Implementado por:** Erick Belo

**Onde está:** `dominio-avaliacao/src/main/java/g8/acadtrack/dominioavaliacao/retificacao/SolicitacaoRetificacaoFabrica.java`

**O problema resolvido:** o construtor de `SolicitacaoRetificacao` aceita todos os campos, incluindo `id`, `justificativaDecisao` e `status`. Isso é necessário para reconstituir objetos vindos do banco. Mas ao criar uma nova solicitação, três desses valores são sempre fixos: `id=null` (ainda não persistido), `justificativaDecisao=null` (nenhuma decisão tomada ainda) e `status=PENDENTE` (estado inicial obrigatório). Sem a factory, o chamador precisaria conhecer esses invariantes:

```java
// Antes: conhecimento de domínio vazando para o use case
new SolicitacaoRetificacao(null, notaId, justificativa, null, StatusSolicitacaoRetificacao.PENDENTE)

// Depois: intenção clara, invariante protegida pelo domínio
SolicitacaoRetificacaoFabrica.nova(notaId, justificativa)
```

**Como é usado no fluxo real:** `SolicitarRetificacaoUseCase.executar()` chama `SolicitacaoRetificacaoFabrica.nova(notaId, justificativa)` após validar que a nota existe e não há solicitação em aberto para ela.

---

### Template Method

**Intenção:** definir o esqueleto de um algoritmo em uma classe abstrata, delegando etapas específicas para subclasses, sem permitir que a ordem das etapas seja alterada.

**Implementado por:** Rodrigo Torres

**Onde está:**
- `aplicacao/src/main/java/g8/acadtrack/aplicacao/nota/FluxoAnaliseAcademicaTemplate.java` — classe abstrata
- `aplicacao/src/main/java/g8/acadtrack/aplicacao/nota/AnalisarDesempenhoAcademicoUseCase.java` — subclasse concreta

**Como funciona:** `FluxoAnaliseAcademicaTemplate.executar()` é `final` — ninguém pode alterar a sequência. Ele chama quatro etapas em ordem fixa:

```java
public final AnaliseDesempenhoAcademicoResultado executar(Long alunoId) {
    List<Nota> notas = buscarNotas(alunoId);          // etapa 1 — abstrata
    validarNotas(notas);                               // etapa 2 — com implementação padrão
    double mediaGeral = calcularMediaGeral(notas);     // etapa 3 — abstrata
    SituacaoAcademica situacao = calcularSituacaoAcademica(mediaGeral); // etapa 4 — abstrata
    return montarResultado(alunoId, notas, mediaGeral, situacao);       // etapa 5 — abstrata
}
```

**Como é usado no fluxo real:** `AlunoController.GET /alunos/{alunoId}/desempenho` chama `analisarDesempenhoAcademicoUseCase.executar(alunoId)`. O Template Method garante que qualquer análise sempre valide a existência de notas antes de calcular.

---

### Decorator

**Intenção:** adicionar responsabilidades a um objeto dinamicamente, encadeando objetos que implementam a mesma interface. Cada elo da cadeia faz sua validação e repassa para o próximo.

**Implementado por:** Erick Belo

**Onde está:**
- `aplicacao/src/main/java/g8/acadtrack/aplicacao/nota/validacao/ValidadorLancamentoNota.java` — interface
- `aplicacao/src/main/java/g8/acadtrack/aplicacao/nota/validacao/ValidadorLancamentoNotaDecorator.java` — abstract
- Seis decoradores concretos na mesma pasta

**Cadeia de validação montada em `ValidacaoLancamentoNotaService`:**

```java
this.cadeiaValidacao =
  new ValidadorValorNotaDecorator(               // valor entre 0 e 10
    new ValidadorEntidadesLancamentoNotaDecorator( // aluno/simulado/disciplina existem
      new ValidadorAlunoAtivoDecorator(           // aluno não pode estar inativo
        new ValidadorDisciplinaAtivaDecorator(    // disciplina não pode estar inativa
          new ValidadorDisciplinaVinculadaSimuladoDecorator( // disciplina ∈ composição
            new ValidadorNotaDuplicadaDecorator(  // par (aluno, simulado, disciplina) único
              new ValidadorLancamentoNotaBase()   // elo final — sem validação
            , notaRepository)
          )
        , disciplinaRepository)
      )
    , alunoRepository, simuladoRepository, disciplinaRepository)
  );
```

**Como é usado no fluxo real:** `LancarNotaUseCase.executar()` chama `validacaoLancamentoNotaService.validar(alunoId, simuladoId, disciplinaId, valor)`. A cadeia inteira é disparada em sequência. Se qualquer elo falhar, uma `RegraDeNegocioException` ou `EntidadeNaoEncontradaException` é lançada e o lançamento é abortado.

---

### Observer

**Intenção:** definir uma dependência um-para-muitos entre objetos para que, quando um objeto mudar de estado, todos os seus dependentes sejam notificados automaticamente — sem acoplamento direto.

**Implementado por:** Erick Belo

**Onde está:**
- `dominio-compartilhado/evento/DomainEvent.java` — interface base de todos os eventos
- `dominio-academico/aluno/evento/RiscoAcademicoEvent.java` — evento concreto (record)
- `aplicacao/evento/DomainEventPublisher.java` — porta de publicação (interface no domínio)
- `infraestrutura/evento/SpringDomainEventPublisher.java` — implementação via `ApplicationEventPublisher`
- `aplicacao/riscoacademico/NotificarResponsavelRiscoAcademicoHandler.java` — handler (observer)

**Fluxo real completo:**

```
LancarNotaUseCase.executar()
  → aluno.registrarRiscoAcademicoIdentificado(nivelRisco)
       → Aluno adiciona RiscoAcademicoEvent à lista interna (se risco ≠ BAIXO)
  → domainEventPublisher.publicar(aluno.liberarEventosDominio())
       → SpringDomainEventPublisher chama applicationEventPublisher.publishEvent(evento)
            → @TransactionalEventListener(AFTER_COMMIT) em NotificarResponsavelRiscoAcademicoHandler
                 → Verifica se aluno tem responsável vinculado e ativo
                 → Cria NotificacaoResponsavel com prioridade (ALTA para risco ALTO, MEDIA para MODERADO)
                 → Persiste via NotificacaoResponsavelRepository
```

O handler usa `@TransactionalEventListener(phase = AFTER_COMMIT)`: a notificação só é criada se a transação de lançamento de nota for confirmada com sucesso. Se a nota falhar por erro de banco, nenhuma notificação espúria é gerada.

---

### Proxy

**Intenção:** fornecer um substituto para outro objeto, controlando o acesso a ele. O proxy e o objeto real implementam a mesma interface; o proxy intercepta a chamada antes de delegar.

**Implementado por:** João Marcelo

**Onde está:**
- `aplicacao/responsavel/AcessoResponsavelAlunoService.java` — interface comum
- `aplicacao/responsavel/AlunoServiceReal.java` — objeto real (busca o aluno no repositório)
- `aplicacao/responsavel/AcessoResponsavelAlunoProxy.java` — proxy `@Primary`
- `aplicacao/responsavel/ValidarAcessoResponsavelAlunoUseCase.java` — lógica de validação

**Como funciona:**

```java
// AcessoResponsavelAlunoProxy — @Primary garante que Spring injeta o proxy em vez do real
@Override
public Aluno executar(Long alunoId, Long responsavelId, PermissaoResponsavel permissao) {
    // 1. Valida ANTES de delegar — se falhar, real nunca é chamado
    validarAcessoResponsavelAlunoUseCase.executar(alunoId, responsavelId, permissao);
    // 2. Delega ao serviço real somente se a validação passou
    return alunoServiceReal.executar(alunoId, responsavelId, permissao);
}
```

`ValidarAcessoResponsavelAlunoUseCase` busca o aluno por ID e chama `aluno.validarAcessoResponsavel(responsavelId, permissao)` — que verifica vínculo ativo e permissão específica, lançando `AcessoDenegadoException` se negado.

**Como é usado no fluxo real:** qualquer endpoint `GET /responsaveis/{responsavelId}/alunos/{alunoId}/notas|simulados|desempenho` injeta `AcessoResponsavelAlunoService`. Como o proxy tem `@Primary`, ele é sempre quem responde. O responsável jamais alcança os dados sem vínculo ativo e permissão.

---

### Strategy

**Intenção:** definir uma família de algoritmos intercambiáveis, encapsulando cada um em uma classe e tornando-os substituíveis sem alterar o código que os usa.

**Implementado por:** Rodrigo Torres

**Onde está:**
- `aplicacao/nota/risco/EstrategiaClassificacaoRiscoAcademico.java` — interface
- `aplicacao/nota/risco/RiscoAltoStrategy.java` — `@Order(1)`
- `aplicacao/nota/risco/RiscoModeradoStrategy.java` — `@Order(2)`
- `aplicacao/nota/risco/RiscoBaixoStrategy.java` — `@Order(3)` (fallback)
- `aplicacao/nota/risco/ClassificadorRiscoAcademicoService.java` — contexto

**Critérios de cada strategy:**

| Strategy | Condição de `aplica()` | Nível |
|---|---|---|
| `RiscoAltoStrategy @Order(1)` | `mediaGeral < 5.0 && simuladosComBaixoDesempenho >= 2` | `ALTO` |
| `RiscoModeradoStrategy @Order(2)` | `mediaGeral < 6.0 && simuladosComBaixoDesempenho == 1` | `MODERADO` |
| `RiscoBaixoStrategy @Order(3)` | `return true` (fallback universal) | `BAIXO` |

**Como funciona no contexto:**

```java
// ClassificadorRiscoAcademicoService — Spring injeta a lista ordenada por @Order
public NivelRiscoAcademico classificar(double mediaGeral, long simuladosComBaixoDesempenho) {
    return estrategias.stream()                                  // [Alto, Moderado, Baixo]
            .filter(e -> e.aplica(mediaGeral, simuladosComBaixoDesempenho))
            .findFirst()                                         // primeira que se aplica
            .map(EstrategiaClassificacaoRiscoAcademico::nivel)
            .orElse(NivelRiscoAcademico.BAIXO);
}
```

**Como é usado no fluxo real:** chamado em `LancarNotaUseCase` (via `AnalisarRiscoAcademicoAlunoService`), em `AnalisarDesempenhoAcademicoUseCase` e em `GerarRankingAcademicoUseCase` — toda classificação de risco do sistema passa por aqui.

---

### Iterator

**Intenção:** prover uma forma de percorrer sequencialmente os elementos de uma coleção sem expor sua representação interna.

**Implementado por:** Rodrigo Torres

**Onde está:**
- `aplicacao/ranking/RankingAcademicoIterator.java` — interface (`hasNext()`, `next()`)
- `aplicacao/ranking/ListaRankingAcademicoIterator.java` — implementação sobre `List<RankingAcademicoItem>`
- `aplicacao/ranking/GerarRankingAcademicoUseCase.java` — consumidor

**Como é usado no fluxo real:**

```java
// GerarRankingAcademicoUseCase.executar(limite, criterio)
List<RankingAcademicoItem> ordenados = ordenarRankingAcademicoService.ordenar(itens, criterio);
RankingAcademicoIterator iterator = new ListaRankingAcademicoIterator(ordenados);
List<RankingAcademicoItem> resultado = new ArrayList<>(quantidadeMaxima);

while (iterator.hasNext() && resultado.size() < quantidadeMaxima) {
    resultado.add(iterator.next());
}
```

O `limite` controla quantos itens são consumidos do iterator. O ranking completo pode ter dezenas de alunos, mas `GET /rankings?limite=10` retorna apenas os Top 10 — o iterator encapsula o controle de parada sem expor o índice.

---

> Documentação completa com justificativas: [`docs/padroes_entrega2.md`](docs/padroes_entrega2.md)

---

## 9. Camada de persistência

### Visão geral

A persistência segue o padrão **Repository Adapter**: interfaces de repositório são declaradas nos módulos de domínio (sem Spring, sem JPA); as implementações ficam em `infraestrutura/`, que é o único módulo autorizado a conhecer JPA.

```
Domínio declara:         AlunoRepository (interface pura)
Infraestrutura implementa: AlunoRepositoryJpa implements AlunoRepository
                                ↓ delega para
                          AlunoSpringDataRepository extends JpaRepository<AlunoJpaEntity, Long>
```

### Entidades JPA

Nove classes `@Entity` em `infraestrutura/src/main/java/g8/acadtrack/infraestrutura/persistencia/entidade/`:

| Entidade JPA | Tabela | Relacionamentos mapeados |
|---|---|---|
| `AlunoJpaEntity` | `aluno` | `@ManyToOne` turma (FK), `@ManyToOne` responsavel (FK) |
| `TurmaJpaEntity` | `turma` | — |
| `DisciplinaJpaEntity` | `disciplina` | `@OneToMany` notas (bidirecional, mappedBy="disciplina") |
| `SimuladoJpaEntity` | `simulado` | `@OneToMany` disciplinas (cascade=ALL, orphanRemoval=true) |
| `SimuladoDisciplinaJpaEntity` | `simulado_disciplina` | `@ManyToOne` simulado (bidirecional) |
| `NotaJpaEntity` | `nota` | `@ManyToOne` disciplina (bidirecional) |
| `SolicitacaoRetificacaoJpaEntity` | `solicitacao_retificacao` | — |
| `ResponsavelJpaEntity` | `responsavel` | — |
| `NotificacaoResponsavelJpaEntity` | `notificacao_responsavel` | — |

**Convenção de mapeamento:** as entidades JPA **não são** as entidades de domínio. Cada `*JpaEntity` é uma classe separada com anotações JPA. Os adaptadores de repositório (`*RepositoryJpa`) convertem manualmente entre JPA entity e objeto de domínio no método `salvar()` e nas consultas.

### Flyway — gerenciamento do schema

O schema do banco é criado e versionado pelo **Flyway**, não pelo Hibernate. O arquivo de migração está em:

```
infraestrutura/src/main/resources/db/migration/V1__schema.sql
```

Ele cria as 9 tabelas com todas as foreign keys e constraints. O Hibernate usa `ddl-auto=validate` — apenas verifica que as tabelas correspondem aos mapeamentos JPA, nunca altera o schema.

**Comportamento na primeira execução:**
1. Flyway detecta banco vazio → executa `V1__schema.sql` → cria as 9 tabelas
2. Hibernate valida o schema → passa, pois as tabelas foram criadas pelo SQL

**Comportamento em execuções seguintes (banco já existe):**
1. `flyway.baseline-on-migrate=true` e `flyway.baseline-version=1` → Flyway reconhece que o banco já está na versão 1 e não tenta recriar as tabelas
2. Hibernate valida → passa

**Configuração em `apresentacao-backend/src/main/resources/application.properties`:**

```properties
spring.datasource.url=jdbc:h2:file:./data/acadtrack-db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=1
```

### Spring Data Repositories

Nove interfaces `JpaRepository` em `infraestrutura/persistencia/springdata/`, com queries derivadas do nome do método:

```java
// Exemplo: AlunoSpringDataRepository
boolean existsByEmailIgnoreCase(String email);
List<AlunoJpaEntity> findByResponsavelId(Long responsavelId);
List<AlunoJpaEntity> findByIdIn(List<Long> ids);

// NotaSpringDataRepository
boolean existsByAlunoIdAndSimuladoIdAndDisciplinaId(Long a, Long s, Long d);
List<NotaJpaEntity> findByAlunoIdIn(List<Long> alunoIds);
```

> Documentação completa: [`docs/persistencia_orm_entrega2.md`](docs/persistencia_orm_entrega2.md)

---

## 10. Camada de apresentação web

A camada de apresentação é composta por dois elementos independentes: uma API REST (módulo `apresentacao-backend`) e uma SPA em JavaScript puro (módulo `apresentacao-frontend`).

### SPA — Single Page Application

A SPA está em `apresentacao-frontend/src/main/resources/static/` e é servida pelo Spring Boot como recurso estático. Não há build JS — são arquivos plain HTML/CSS/JS.

**Estrutura:**

```
static/
├── index.html     8 seções de navegação lateral
├── styles.css
├── app.js         2.300+ linhas — lógica de todas as telas
└── js/
    ├── apiClient.js    fetch com tratamento de erro centralizado
    ├── config.js       base URL da API
    ├── errors.js       mapeamento de erros HTTP → mensagens amigáveis
    ├── navigation.js   roteamento entre seções
    ├── session.js      perfil selecionado (persona)
    ├── store.js        estado compartilhado entre módulos
    ├── utils.js        formatação, normalização
    └── views/ui.js     helpers de renderização
```

**Seções disponíveis na interface:**

| Seção | Navegação lateral | Funcionalidades |
|---|---|---|
| Alunos | `#alunos` | Cadastrar, editar, ativar/inativar, vincular turma |
| Disciplinas | `#disciplinas` | CRUD completo, ativar/inativar |
| Notas / Histórico de Notas | `#notas` | Professor: lançar nota (dropdowns dinâmicos). Aluno: histórico de notas por simulado e disciplina |
| Desempenho / Análise de Desempenho | `#desempenho` | Análise consolidada com média, situação, nível de risco acadêmico, ranking, histórico por simulado |
| Simulados | `#simulados` | Criar com seleção de disciplinas por checkbox (mín 2), detalhar, editar |
| Retificações | `#retificacoes` | Solicitar (visão aluno), listar e decidir com aprovação/reprovação (visão professor) |
| Responsáveis | `#responsaveis` | Cadastrar, vincular com 3 permissões, desvincular, excluir |
| Notificações | `#notificacoes` | Listar alertas de risco por responsável, marcar como lida |
| Portal responsável | `#portal` | Consultar notas/simulados/desempenho do aluno vinculado |

### API REST — Controllers

Oito `@RestController` em `apresentacao-backend/src/main/java/g8/acadtrack/apresentacao/controller/`:

#### Turmas — `GET/POST /turmas`

| Método | Rota | Status |
|---|---|---|
| GET | `/turmas` | 200 |
| POST | `/turmas` | 201, 400, 409 |

> As turmas `1º A`, `1º B`, `2º A`, `2º B`, `3º A`, `3º B` são criadas automaticamente na inicialização.

#### Alunos — `/alunos`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/alunos` | Lista todos | 200 |
| GET | `/alunos/{alunoId}` | Busca por ID | 200, 404 |
| POST | `/alunos` | Cadastra (e-mail único) | 201, 400, 409 |
| PATCH | `/alunos/{alunoId}` | Edita nome e/ou e-mail | 200, 400, 404, 409 |
| PATCH | `/alunos/{alunoId}/inativar` | Soft-inativar | 200, 404 |
| PATCH | `/alunos/{alunoId}/ativar` | Reativar | 200, 404 |
| PUT | `/alunos/{alunoId}/turma` | Definir ou trocar turma | 200, 400, 404 |
| PUT | `/alunos/{alunoId}/responsavel` | Vincular responsável com permissões | 200, 400, 404, 409 |
| DELETE | `/alunos/{alunoId}/responsavel` | Desvincular responsável | 200, 404 |
| GET | `/alunos/{alunoId}/desempenho` | Análise consolidada de desempenho | 200, 400, 404 |

```json
// POST /alunos
{ "nome": "João Silva", "email": "joao.silva@escola.edu" }

// PUT /alunos/{id}/responsavel — ao menos uma permissão true
{ "responsavelId": 1, "podeVisualizarNotas": true,
  "podeVisualizarSimulados": true, "podeVisualizarDesempenho": false }

// GET /alunos/{id}/desempenho — response
{ "alunoId": 1, "mediaGeral": 7.25, "situacaoAcademica": "APROVADO",
  "nivelRisco": "BAIXO", "riscoAcademico": false,
  "posicaoRanking": 2, "totalAlunosRanking": 5, "alunoNoTop10": true,
  "historicoSimulados": [...], "notasPorDisciplina": [...] }
```

#### Disciplinas — `/disciplinas`

| Método | Rota | Status |
|---|---|---|
| GET | `/disciplinas` | 200 |
| GET | `/disciplinas/{id}` | 200, 404 |
| POST | `/disciplinas` | 201, 400, 409 |
| PATCH | `/disciplinas/{id}` | 200, 400, 404, 409 |
| PATCH | `/disciplinas/{id}/inativar` | 200, 400, 404 |
| PATCH | `/disciplinas/{id}/ativar` | 200, 400, 404 |
| DELETE | `/disciplinas/{id}` | 204, 400, 404 |

#### Simulados — `/simulados`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/simulados` | Lista com qtd de disciplinas | 200 |
| GET | `/simulados/{id}` | Detalha composição, notas e alunos | 200, 404 |
| GET | `/simulados/{id}/disciplinas` | Lista disciplinas vinculadas | 200, 404 |
| POST | `/simulados` | Cria (mín 2 disciplinas ativas) | 201, 400, 404, 409 |
| PATCH | `/simulados/{id}` | Edita (bloqueado se tem notas) | 200, 400, 404 |

```json
// POST /simulados
{ "descricao": "Simulado 1 - Bimestre", "disciplinasIds": [1, 2] }
```

#### Notas — `/notas`

| Método | Rota | Status |
|---|---|---|
| POST | `/notas` | 201, 400, 404, 409 |
| GET | `/notas/aluno/{alunoId}` | 200, 404 |
| GET | `/notas/aluno/{alunoId}/simulado/{simuladoId}/media` | 200, 400, 404 |

```json
// POST /notas
{ "alunoId": 1, "simuladoId": 1, "disciplinaId": 1, "valor": 7.5 }
```

#### Rankings — `/rankings`

| Método | Rota | Status |
|---|---|---|
| GET | `/rankings?limite=10&criterio=MEDIA_DESC` | 200 |
| GET | `/rankings/{simuladoId}` | 200, 400, 404 |

#### Responsáveis — `/responsaveis`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/responsaveis` | Lista todos | 200 |
| POST | `/responsaveis` | Cadastra (e-mail único) | 201, 400, 409 |
| DELETE | `/responsaveis/{id}` | Exclui e limpa vínculos | 204, 404 |
| GET | `/responsaveis/{id}/alunos/{alunoId}/notas` | Notas (requer VISUALIZAR_NOTAS) | 200, 403, 404 |
| GET | `/responsaveis/{id}/alunos/{alunoId}/simulados` | Simulados (requer VISUALIZAR_SIMULADOS) | 200, 403, 404 |
| GET | `/responsaveis/{id}/alunos/{alunoId}/desempenho` | Desempenho (requer VISUALIZAR_DESEMPENHO) | 200, 400, 403, 404 |
| GET | `/responsaveis/{id}/notificacoes` | Lista notificações | 200, 404 |
| PATCH | `/responsaveis/{id}/notificacoes/{notifId}/lida` | Marca como lida | 200, 404 |

#### Retificações — `/retificacoes`

| Método | Rota | O que faz | Status |
|---|---|---|---|
| GET | `/retificacoes` | Lista com aluno, disciplina, simulado | 200 |
| GET | `/retificacoes/{id}` | Detalha | 200, 404 |
| POST | `/retificacoes` | Solicita (justificativa obrigatória) | 201, 400, 404, 409 |
| PATCH | `/retificacoes/{id}/em-analise` | PENDENTE → EM_ANALISE | 200, 404, 409 |
| PATCH | `/retificacoes/{id}/aprovar` | EM_ANALISE → APROVADA (atualiza nota) | 200, 400, 404, 409 |
| PATCH | `/retificacoes/{id}/reprovar` | EM_ANALISE → REPROVADA (nota intacta) | 200, 400, 404, 409 |

```json
// POST /retificacoes
{ "notaId": 1, "justificativa": "Houve erro na correção da questão discursiva" }

// PATCH /retificacoes/{id}/aprovar — justificativa de decisão obrigatória
{ "novoValorNota": 9.0, "justificativaDecisao": "Erro confirmado na correção" }
```

### Swagger UI

Todos os endpoints estão documentados e são testáveis interativamente em:

```
http://localhost:8080/swagger-ui/index.html
```

### Tratamento de erros

`GlobalExceptionHandler` mapeia exceções de domínio para status HTTP:

| Exceção de domínio | Status HTTP |
|---|---|
| `EntidadeNaoEncontradaException` | 404 Not Found |
| `RegraDeNegocioException` | 400 Bad Request |
| `ConflitoDeEstadoException` | 409 Conflict |
| `AcessoDenegadoException` | 403 Forbidden |

---

## 11. Como rodar o projeto

### Pré-requisitos

| Requisito | Versão mínima | Como verificar |
|---|---|---|
| JDK | 17 | `java -version` |
| Maven | 3.8+ | `mvn -version` (ou use o wrapper incluso) |

Não é necessário instalar banco de dados. O H2 é embutido; o Flyway cria as tabelas automaticamente na primeira execução.

---

### Opção 1 — Docker (sem instalar JDK ou Maven)

Requisito: [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e em execução.

```bash
docker compose up --build
```

Build demora ~2 minutos na primeira vez. Quando aparecer `Started AcadTrackApplication`, acesse `http://localhost:8080`.

| Comando | O que faz |
|---|---|
| `docker compose up -d` | Sobe em segundo plano |
| `docker compose logs -f` | Acompanha logs em tempo real |
| `docker compose down` | Para e remove containers |
| `docker compose up --build -d` | Rebuild após mudança de código |

---

### Opção 2 — Rodando localmente

> **Recomendado:** use os scripts abaixo — eles liberam a porta 8080 automaticamente antes de subir, evitando o erro `Process terminated with exit code: 1` quando um processo anterior ainda está rodando.

#### Windows (PowerShell) — recomendado

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\start-backend.ps1
```

#### Linux / macOS / Git Bash — recomendado

```bash
bash scripts/start-backend.sh
```

#### Comando direto (sem liberação automática de porta)

```powershell
# Windows
.\mvnw.cmd -pl apresentacao-backend -am spring-boot:run
```

```bash
# Linux / macOS / Git Bash
./mvnw -pl apresentacao-backend -am spring-boot:run
```

O flag `-am` (also-make) constrói todos os módulos dos quais `apresentacao-backend` depende antes de subir a aplicação.

> **Erro `Process terminated with exit code: 1`?** A porta 8080 está ocupada por uma instância anterior. Use o script acima ou execute no PowerShell: `$p = Get-NetTCPConnection -LocalPort 8080 -State Listen | Select -Expand OwningProcess -First 1; Stop-Process -Id $p -Force`

#### Quando a aplicação estiver rodando

```
Started AcadTrackApplication in X.XXX seconds
```

| URL | O que é |
|---|---|
| `http://localhost:8080` | Interface web (SPA) |
| `http://localhost:8080/swagger-ui/index.html` | Documentação interativa da API |
| `http://localhost:8080/h2-console` | Console H2 para inspecionar o banco |

No H2 Console, use JDBC URL: `jdbc:h2:file:./data/acadtrack-db`

> **Localização do arquivo:** o banco é persistido em `apresentacao-backend/data/acadtrack-db.mv.db` (relativo à raiz do projeto). O `./data/` na JDBC URL é relativo ao diretório de trabalho do módulo `apresentacao-backend/`, não à raiz do projeto.  
> **Para resetar todos os dados:** pare o backend e delete `apresentacao-backend/data/acadtrack-db.mv.db`. Na próxima inicialização o Flyway recria o schema e o `DadosIniciaisConfig` semeará as 6 turmas padrão.

#### Porta alternativa (se 8080 estiver ocupada)

```powershell
.\mvnw.cmd -pl apresentacao-backend -am spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

O script `scripts/run-backend.ps1` detecta automaticamente a primeira porta livre entre 8080 e 8299:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run-backend.ps1
```

Para liberar portas travadas:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\free-ports-if-needed.ps1
```

---

### Ordem de criação de dados (para testar o fluxo completo)

Os recursos têm dependências entre si. Na primeira vez que usar o sistema, crie nesta ordem:

```
1. Turmas         — já criadas automaticamente (1º A … 3º B)
2. Disciplinas    — criar no mínimo 2 para usar em simulados
3. Alunos         — cadastrar com nome e e-mail
4. Responsáveis   — cadastrar e vincular ao aluno com permissões
5. Vincular aluno → turma
6. Simulados      — criar com pelo menos 2 disciplinas ativas
7. Notas          — lançar para aluno + simulado + disciplina
8. Retificações   — solicitar, analisar, aprovar/reprovar
```

---

### Rodando os testes

```powershell
# Suite completa: 77 cenários Cucumber + 24 JUnit = 101 testes
.\mvnw.cmd test

# Apenas o módulo BDD
.\mvnw.cmd test -pl bdd/acadtrackbdd -am

# Build completo de todos os módulos sem testes
.\mvnw.cmd install -DskipTests
```

**Resultado esperado ao rodar os testes:**
```
Tests run: 101, Failures: 0, Errors: 0, Skipped: 0 — BUILD SUCCESS
```

---

## Documentação acadêmica

| Artefato | Localização |
|---|---|
| Descrição do domínio + Linguagem Onipresente | [`docs/descricao_do_dominio.md`](docs/descricao_do_dominio.md) |
| DDD nos 4 níveis | [`docs/ddd_niveis.md`](docs/ddd_niveis.md) |
| Arquitetura Limpa | [`docs/arquitetura_limpa.md`](docs/arquitetura_limpa.md) |
| Story Map + personas | [`docs/story_map_personas.md`](docs/story_map_personas.md) · [`docs/story_map.pdf`](docs/story_map.pdf) |
| Protótipos (Figma + capturas) | [`docs/prototipos.md`](docs/prototipos.md) |
| Modelo CML (Context Mapper) | [`acadtrack.cml`](acadtrack.cml) |
| Bounded Contexts | [`docs/cml/bounded_contexts.md`](docs/cml/bounded_contexts.md) |
| BDD — abordagem e cobertura | [`docs/bdd.md`](docs/bdd.md) |
| Padrões de projeto | [`docs/padroes_entrega2.md`](docs/padroes_entrega2.md) |
| Persistência ORM/JPA + Flyway | [`docs/persistencia_orm_entrega2.md`](docs/persistencia_orm_entrega2.md) |
| Roteiro de demonstração | [`docs/script_demonstracao.md`](docs/script_demonstracao.md) |
| Guia Swagger passo a passo | [`docs/demo_fluxo_swagger_passo_a_passo.md`](docs/demo_fluxo_swagger_passo_a_passo.md) |

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
| **Rodrigo Torres** | rtgf@cesar.school | F4 (Análise de desempenho), F5 (Simulados); Template Method, Strategy, Iterator |
