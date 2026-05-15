# Documentação — AcadTrack

Esta pasta centraliza todos os artefatos acadêmicos e técnicos do projeto. O README raiz do repositório ([`../README.md`](../README.md)) é o ponto de entrada para quem quer rodar o sistema; este arquivo é o índice do que existe aqui dentro.

---

## Índice de arquivos

| Arquivo / Pasta | Conteúdo |
|---|---|
| [`descricao_do_dominio.md`](descricao_do_dominio.md) | Descrição do domínio, Linguagem Onipresente, definição das duas médias (global simples vs. por simulado), regras de negócio alinhadas ao código |
| [`ddd_niveis.md`](ddd_niveis.md) | DDD nos 4 níveis: preliminar, estratégico, tático e operacional |
| [`arquitetura_limpa.md`](arquitetura_limpa.md) | Clean Architecture aplicada ao projeto: camadas, dependências, justificativas |
| [`story_map_personas.md`](story_map_personas.md) | Story Map com personas (Coordenador, Professor, Aluno, Responsável) e backlog por release |
| [`story_map.pdf`](story_map.pdf) | Story Map visual em PDF |
| [`prototipos.md`](prototipos.md) | Referência ao protótipo Figma e capturas da interface implementada |
| [`bdd.md`](bdd.md) | Abordagem BDD adotada, organização dos cenários, como interpretar os `.feature` |
| [`padroes_entrega2.md`](padroes_entrega2.md) | Descrição dos 6 padrões de projeto implementados na Entrega 2 |
| [`persistencia_orm_entrega2.md`](persistencia_orm_entrega2.md) | Mapeamento objeto-relacional: entidades JPA, relacionamentos, configuração H2 |
| [`funcionalidades.md`](funcionalidades.md) | Detalhamento das 6 funcionalidades oficiais |
| [`script_demonstracao.md`](script_demonstracao.md) | Roteiro passo a passo para demonstração via Swagger ou scripts |
| [`demo_fluxo_swagger_passo_a_passo.md`](demo_fluxo_swagger_passo_a_passo.md) | Guia específico para execução do fluxo completo pelo Swagger UI |
| [`validacoes.md`](validacoes.md) | Catálogo de validações com prints; imagens em [`validacoes/`](validacoes/) |
| [`checklist_entrega2.md`](checklist_entrega2.md) | Checklist técnico da Entrega 2 |
| [`cml/acadtrack.cml`](cml/acadtrack.cml) | Modelo Context Mapper com 3 Bounded Contexts e Context Map |
| [`cml/bounded_contexts.md`](cml/bounded_contexts.md) | Resumo textual dos bounded contexts e suas responsabilidades |
| [`img/`](img/) | Capturas de tela da interface (9 PNGs: dashboard, alunos, disciplinas, notas, simulado, desempenho, retificação, responsáveis, portal do responsável) |
| [`validacoes/`](validacoes/) | Capturas de validações da API (prints de erros 400/409/403/404) |

---

## Observações sobre o domínio

### Duas médias, propósitos distintos

O sistema mantém intencionalmente duas formas de calcular a média do aluno:

- **Média global simples**: média aritmética de *todas* as notas do aluno, sem peso e sem filtro por simulado. É a base para `SituacaoAcademica` (APROVADO / RECUPERACAO / REPROVADO) persistida no cadastro do `Aluno`. Recalculada em `AvaliacaoAcademicaService` após lançamento de nota ou aprovação de retificação.

- **Média por simulado**: média das notas do aluno restrita às disciplinas da composição de um simulado, com peso padrão interno `1.0`. Usada em `CalcularMediaPonderadaUseCase`, rankings e na análise de desempenho histórico por avaliação.

### Personas técnicas vs. de negócio

- **Coordenador** e **Professor** são personas de negócio documentadas (story map, cenários BDD). Nesta entrega, não existem como roles técnicas de autenticação no backend — as ações de cada persona são expostas por controllers abertos.
- **Responsável** é a única persona com controle de acesso implementado no código (via `AcessoResponsavelAlunoProxy` e `PermissaoResponsavel`).

### Versão do JDK

O `pom.xml` define Java 17 como versão mínima (requisito Spring Boot 3.x). O projeto foi validado com sucesso também em JDK 25.

---

## Scripts de automação

Os scripts PowerShell em [`../scripts/`](../scripts/) automatizam tarefas de desenvolvimento e demonstração. Consulte [`../scripts/README.md`](../scripts/README.md) para descrição de cada um.
