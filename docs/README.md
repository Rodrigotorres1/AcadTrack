# Documentação — AcadTrack

Esta pasta centraliza todos os artefatos acadêmicos e técnicos do projeto. O README raiz ([`../README.md`](../README.md)) é o ponto de entrada para quem quer entender e rodar o sistema; este arquivo é o índice do que existe aqui dentro.

---

## Índice de arquivos

| Arquivo / Pasta | Conteúdo |
|---|---|
| [`descricao_do_dominio.md`](descricao_do_dominio.md) | Descrição do domínio, Linguagem Onipresente, definição das duas médias (global simples vs. por simulado), atores do sistema |
| [`ddd_niveis.md`](ddd_niveis.md) | DDD nos 4 níveis: preliminar, estratégico, tático e operacional |
| [`arquitetura_limpa.md`](arquitetura_limpa.md) | Clean Architecture aplicada ao projeto: camadas, dependências, justificativas |
| [`story_map_personas.md`](story_map_personas.md) | Story Map com personas (Coordenador, Professor, Aluno, Responsável) e backlog por release |
| [`story_map.pdf`](story_map.pdf) | Story Map visual em PDF |
| [`prototipos.md`](prototipos.md) | Referência ao protótipo Figma e capturas das 9 telas da interface implementada |
| [`bdd.md`](bdd.md) | Abordagem BDD: 14 features, 77 cenários, justificativa de cobertura por feature |
| [`padroes_entrega2.md`](padroes_entrega2.md) | Os 7 padrões de projeto implementados: Factory, Template Method, Decorator, Observer, Proxy, Strategy, Iterator |
| [`persistencia_orm_entrega2.md`](persistencia_orm_entrega2.md) | Mapeamento objeto-relacional: 9 entidades JPA, relacionamentos, Flyway, ddl-auto=validate |
| [`funcionalidades.md`](funcionalidades.md) | Detalhamento das 6 funcionalidades principais (F1–F6) |
| [`script_demonstracao.md`](script_demonstracao.md) | Roteiro passo a passo para demonstração via Swagger ou scripts PowerShell |
| [`demo_fluxo_swagger_passo_a_passo.md`](demo_fluxo_swagger_passo_a_passo.md) | Guia para execução do fluxo completo pelo Swagger UI |
| [`validacoes.md`](validacoes.md) | Catálogo de validações com prints de respostas de erro (400/403/404/409) |
| [`checklist_entrega2.md`](checklist_entrega2.md) | Checklist técnico da Entrega 2 |
| [`../acadtrack.cml`](../acadtrack.cml) | Modelo Context Mapper na raiz: 4 Bounded Contexts, Context Map, Aggregates, Events |
| [`cml/bounded_contexts.md`](cml/bounded_contexts.md) | Resumo textual dos bounded contexts e suas responsabilidades |
| [`img/`](img/) | 9 capturas de tela da interface: dashboard, alunos, disciplinas, notas, simulado, desempenho, retificação, responsáveis, portal do responsável |
| [`validacoes/`](validacoes/) | Capturas de validações da API (prints de erros de negócio) |

---

## Observações sobre o domínio

### Duas médias com propósitos distintos

O sistema mantém intencionalmente duas formas de calcular a média do aluno:

- **Média global simples**: aritmética de *todas* as notas do aluno, sem peso e sem filtro por simulado. É a base para `SituacaoAcademica` (APROVADO / RECUPERACAO / REPROVADO) persistida no cadastro do `Aluno`. Recalculada em `AvaliacaoAcademicaService` após lançamento de nota ou aprovação de retificação.

- **Média por simulado**: média das notas do aluno restrita às disciplinas da composição de um simulado, com peso padrão interno `1.0`. Usada em `CalcularMediaPonderadaUseCase`, rankings e na análise de desempenho histórico por avaliação.

### Personas técnicas vs. de negócio

- **Coordenador** e **Professor** são personas de negócio (story map, cenários BDD). Não existem como roles técnicas de autenticação nesta entrega — as ações de cada persona são expostas por controllers abertos.
- **Responsável** é a única persona com controle de acesso implementado no código (via `AcessoResponsavelAlunoProxy` e `PermissaoResponsavel`).

### Persistência

O schema do banco é gerenciado pelo **Flyway** (arquivo `V1__schema.sql` em `infraestrutura/src/main/resources/db/migration/`). O Hibernate usa `ddl-auto=validate` — valida que as tabelas correspondem aos mapeamentos JPA, mas nunca modifica o schema. Nos testes BDD, o Flyway é desabilitado e o Hibernate usa `ddl-auto=update` sobre H2 em memória.

### Versão do JDK

O `pom.xml` define Java 17 como versão mínima (requisito do Spring Boot 3.x). O projeto foi validado com sucesso também em JDK 25.

---

## Scripts de automação

Os scripts PowerShell em [`../scripts/`](../scripts/) automatizam tarefas de desenvolvimento e demonstração. Consulte [`../scripts/README.md`](../scripts/README.md) para descrição de cada um.
