# Organizacao de Codigo

Este documento define convencoes para manter a estrutura do projeto consistente.

## Camadas e responsabilidades

- `dominio-*`: entidades, value objects, invariantes e contratos de repositorio.
- `aplicacao`: casos de uso e orquestracao de fluxo de negocio.
- `infraestrutura`: persistencia, adaptadores JPA e integracoes tecnicas.
- `apresentacao-backend`: controllers, DTOs, tratamento de erro e contrato HTTP.

## Convencoes de nome

- Casos de uso: sufixo `UseCase`.
- Servicos de apoio de aplicacao: sufixo `Service`.
- Controllers: sufixo `Controller`.
- Requests HTTP: sufixo `Request`.
- Responses HTTP: sufixo `Response`.

## DTOs na camada de apresentacao

- `br.com.acadtrack.apresentacao.dto.request`: DTOs de entrada.
- `br.com.acadtrack.apresentacao.dto.response`: DTOs de saida.
- Evitar regras de negocio em DTO; manter apenas validacao de entrada e mapeamento.

## Estruturacao por feature (aplicacao)

Manter pastas por feature funcional:

- `aluno`
- `disciplina`
- `nota`
- `professor`
- `ranking`
- `responsavel`
- `retificacao`
- `simulado`
- `turma`

## Regras de higiene

- Nao manter classes sem referencia em producao/teste.
- Nao versionar artefatos de build (`target/`, `*.class`, `*.jar`).
- Quando remover classe, executar testes do monorepo (`mvn test`) antes de concluir.

## Alteracoes aplicadas nesta rodada

- DTOs movidos fisicamente para:
  - `apresentacao/dto/request`
  - `apresentacao/dto/response`
- Nomenclatura de retificacao padronizada nos casos de uso:
  - `SolicitarRetificacaoUseCase`
  - `AprovarRetificacaoUseCase`
  - `ReprovarRetificacaoUseCase`
- Fluxo legado de ranking em `notas` mantido por compatibilidade, porem marcado como **deprecated**:
  - `GET /notas/ranking`
  - `GET /notas/ranking/top`
- Fluxo recomendado consolidado:
  - `GET /rankings/{simuladoId}`
- CML alinhado ao codigo atual:
  - IDs em `Nota` representados como `Long`
  - removido `DominioCompartilhado` do context map
  - agregados/servicos de retificacao alinhados com `SolicitacaoRetificacao`
