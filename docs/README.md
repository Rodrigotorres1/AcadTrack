# Documentacao do Projeto

Esta pasta centraliza os artefatos da documentacao do AcadTrack.

**JDK:** referência **Java 17+** (Spring Boot / `pom.xml`); no relatório deve coincidir com o enunciado. O grupo validou o build e a API também com **JDK 25** no desenvolvimento (`java -version` / `JAVA_HOME`).

**DDD e CML:** modelagem em arquivo CML [`cml/acadtrack.cml`](cml/acadtrack.cml); resumo em [`cml/bounded_contexts.md`](cml/bounded_contexts.md); níveis em [`ddd_niveis.md`](ddd_niveis.md).

**Medidas de desempenho:** em [`descricao_do_dominio.md`](descricao_do_dominio.md) (secao *Medidas de desempenho*) e na raiz do repo em [`README.md`](../README.md) (secao *Médias e situação acadêmica*), esta explicito o papel da **media global simples** (situacao no aluno) vs **media ponderada por simulado**.

## Indice

- `arquitetura_limpa.md`
- `bdd.md`
- `ddd_niveis.md`
- `descricao_do_dominio.md`
- `validacoes.md` (catálogo); imagens PNG em [`validacoes/`](./validacoes/)
- `funcionalidades.md`
- `prototipos.md`
- `script_demonstracao.md`
- `story_map_personas.md`
- `story_map.pdf`

## Swagger, demonstração e validações

- Roteiro passo a passo (Swagger/Postman, ordem dos fluxos por dependência): `script_demonstracao.md`
- Prints e referência de comandos opcionais: `validacoes.md` (imagens em [`validacoes/`](./validacoes/))

## Scripts de automação (PowerShell)

- Resumo e utilidade de cada script: [`../scripts/README.md`](../scripts/README.md)

## Personas

- Resumo rapido de personas e story map completo em `story_map_personas.md`.

Observacao sobre o Coordenador:

- Nesta entrega, o **Coordenador** esta modelado como persona de negocio (documentacao e BDD), mas ainda nao foi implementado tecnicamente como role/perfil de autenticacao no backend.

## CML e Bounded Contexts

- Modelo CML: `cml/acadtrack.cml`
- Resumo dos contextos: `cml/bounded_contexts.md`

## Checklist de entrega (docs) - principais 

- [x] Estrutura de documentacao centralizada em `docs/`
- [x] Modelo CML consolidado em `docs/cml/acadtrack.cml`
- [x] Story map textual em `docs/story_map_personas.md`
- [x] Story map visual em `docs/story_map.pdf`
