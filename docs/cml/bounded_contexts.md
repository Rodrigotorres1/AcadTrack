# Bounded Contexts do AcadTrack

Este documento resume os bounded contexts definidos no modelo CML do projeto.

## Contextos

### GestaoAcademicaContext
- Responsavel por alunos, turmas, simulados e composicao de disciplinas.
- Casos de uso principais: criar simulado, vincular aluno a turma, configurar disciplina e peso.
- No modelo CML refinado, `Simulado` usa `descricao` e foi incluido agregado explicito de `Disciplina` (ativa/inativa), alinhado ao comportamento da API.

### AvaliacaoDesempenhoContext
- Responsavel por notas, media ponderada, ranking e retificacao de nota.
- Casos de uso principais: lancar nota, calcular media, gerar ranking e tratar retificacao.

### UsuariosContext
- Responsavel por perfis de professor e responsavel.
- Casos de uso principais: vincular e desvincular responsavel.

## Relacoes entre contextos (Context Map)

- `GestaoAcademicaContext -> AvaliacaoDesempenhoContext` (`CUSTOMER_SUPPLIER`)
  - Justificativa: Avaliacao depende de dados academicos (simulado/disciplina) e exige estabilidade de contrato entre os contextos.
- `GestaoAcademicaContext -> UsuariosContext` (`CONFORMIST`)
  - Justificativa: Gestao consome dados de usuarios e se conforma ao contrato publicado por Usuarios.
- `AvaliacaoDesempenhoContext -> UsuariosContext` (`CONFORMIST`)
  - Justificativa: Avaliacao tambem consome usuarios (responsavel/professor) sem impor modelo proprio ao contexto de Usuarios.

## Nota sobre a persona Coordenador

- O **Coordenador** existe como persona de negocio e aparece nos fluxos BDD/documentacao.
- Nesta entrega, ainda nao existe implementacao tecnica de role/perfil de autenticacao "Coordenador" no backend.
