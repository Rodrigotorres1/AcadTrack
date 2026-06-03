# Checklist da Entrega 2

## Requisitos herdados da Entrega 1

- Descricao do dominio: `docs/descricao_do_dominio.md`.
- Linguagem onipresente: secao propria em `docs/descricao_do_dominio.md`.
- Story map/personas: `docs/story_map_personas.md` e `docs/story_map.pdf`.
- Prototipos: `docs/prototipos.md`.
- Modelo CML/Context Mapper: `acadtrack.cml` (raiz do projeto).
- Bounded contexts/context map: `docs/cml/bounded_contexts.md`.
- Cenarios BDD: `bdd/acadtrackbdd/src/test/resources/features`.
- Automacao Cucumber: `bdd/acadtrackbdd` com `CucumberTest`.
- Niveis DDD: `docs/ddd_niveis.md`.
- Arquitetura limpa: `docs/arquitetura_limpa.md`.

## Funcionalidades consolidadas

1. Disciplinas: criar, listar, ativar/inativar, validar uso em simulados/notas.
2. Responsaveis: criar, listar, vincular/desvincular, permissao de acesso e portal.
3. Notas: lancar nota, validar regras, persistir, recalcular media/situacao e disparar notificacao.
4. Desempenho: analisar media, situacao, risco, historico por simulado, notas por disciplina e posicao no ranking.
5. Simulados: criar, listar, detalhar, validar composicao e consistencia.
6. Retificacoes: solicitar, analisar, aprovar/reprovar e refletir no desempenho.

## Padroes de projeto

Detalhamento em `docs/padroes_entrega2.md`.

- Observer: notificacoes automaticas de desempenho/risco.
- Template Method: fluxo padronizado de analise academica.
- Decorator: validacoes extras no lancamento de notas.
- Proxy: controle de acesso do responsavel.
- Strategy: classificacao de risco academico.
- Iterator: percorre colecao ordenada do ranking com limite via RankingAcademicoIterator.

## Persistencia ORM/JPA

Detalhamento em `docs/persistencia_orm_entrega2.md`.

- Entidades com `@Entity`, `@Id`, `@GeneratedValue`.
- Relacionamentos JPA explicitos na infraestrutura.
- Repositories Spring Data JPA.
- Banco relacional H2 configurado.
- Dados salvos e consultados via endpoints reais.
- Web sem dependencia de listas mockadas para os fluxos principais.

## Camada web

O frontend e uma SPA vanilla JS servida pelo Spring Boot a partir de:

- `apresentacao-backend/src/main/resources/static`

O modulo `apresentacao-frontend` e um placeholder Maven conceitual (`packaging=pom`) que documenta a camada de apresentacao dentro da arquitetura multi-modulo. Ver `apresentacao-frontend/README.md` para detalhes.

Telas/abas existentes (10 secoes no HTML):

- Alunos com cadastro/listagem, criacao de turma e vinculo de responsavel.
- Turmas.
- Disciplinas.
- Notas (lancamento e listagem enriquecida com nomes de disciplina e simulado).
- Desempenho com risco, historico por simulado, notas por disciplina, posicao no ranking e Top 10.
- Simulados.
- Retificacoes.
- Responsaveis.
- Notificacoes.
- Portal responsavel.

Entrada por tipo de usuario:

- Coordenador/professor: fluxos administrativos expostos nas abas da aplicacao web.
- Responsavel: aba `Portal responsavel`, com consulta mediada pelo backend.
- Observacao: ainda nao existe login/autenticacao por role tecnica; os perfis estao modelados como personas e fluxos de interface.

## Conferencia final

- Rodar `.\mvnw.cmd test`.
- Confirmar que nao existem referencias a Plano de estudo ou Relatorio academico como abas/funcionalidades.
- Confirmar que a API responde em `/v3/api-docs`.
- Confirmar que a web chama endpoints reais.
- Confirmar que README e docs da Entrega 2 estao atualizados.
