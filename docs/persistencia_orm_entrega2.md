# Persistencia ORM/JPA na Entrega 2

O AcadTrack usa Spring Data JPA/Hibernate na camada de infraestrutura, mantendo o dominio sem anotacoes de framework. As entidades JPA ficam em `infraestrutura/persistencia/entidade` e os adapters de repositorio convertem entre entidades JPA e objetos de dominio.

## Configuracao

- Banco relacional: H2 em arquivo para execucao local.
- Testes BDD/Cucumber: H2 em memoria via `bdd/acadtrackbdd/src/test/resources/application.properties`.
- Arquivo: `apresentacao-backend/src/main/resources/application.properties`.
- URL local atual: `jdbc:h2:file:./data/acadtrack-db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`.
- DDL: `spring.jpa.hibernate.ddl-auto=update`.
- Console H2: `/h2-console`.

## Entidades JPA

Todas possuem `@Entity`, `@Id` e `@GeneratedValue(strategy = GenerationType.IDENTITY)`:

- `AlunoJpaEntity`
- `TurmaJpaEntity`
- `DisciplinaJpaEntity`
- `ResponsavelJpaEntity`
- `NotaJpaEntity`
- `SimuladoJpaEntity`
- `SimuladoDisciplinaJpaEntity`
- `SolicitacaoRetificacaoJpaEntity`
- `NotificacaoResponsavelJpaEntity`

## Relacionamentos mapeados

Os fluxos de dominio trabalham com IDs para preservar a arquitetura limpa. Na infraestrutura, os relacionamentos ORM foram explicitados como associacoes JPA sobre esses IDs:

- aluno -> turma: `AlunoJpaEntity.turma` (`@ManyToOne`)
- turma -> alunos: `TurmaJpaEntity.alunos` (`@OneToMany`)
- aluno -> responsavel: `AlunoJpaEntity.responsavel` (`@ManyToOne`)
- responsavel -> alunos: `ResponsavelJpaEntity.alunos` (`@OneToMany`)
- nota -> aluno: `NotaJpaEntity.aluno` (`@ManyToOne`)
- nota -> disciplina: `NotaJpaEntity.disciplina` (`@ManyToOne`)
- nota -> simulado: `NotaJpaEntity.simulado` (`@ManyToOne`)
- disciplina -> notas: `DisciplinaJpaEntity.notas` (`@OneToMany`)

## Repositories Spring Data

Os contratos de dominio ficam nos modulos de dominio. A infraestrutura implementa esses contratos com adapters `*RepositoryJpa`, usando Spring Data `JpaRepository` em `infraestrutura/persistencia/springdata`.

Exemplos:

- `AlunoSpringDataRepository extends JpaRepository<AlunoJpaEntity, Long>`
- `NotaSpringDataRepository extends JpaRepository<NotaJpaEntity, Long>`
- `TurmaSpringDataRepository extends JpaRepository<TurmaJpaEntity, Long>`
- `NotificacaoResponsavelSpringDataRepository extends JpaRepository<NotificacaoResponsavelJpaEntity, Long>`

## Comprovacao funcional

- Cadastrar aluno persiste em `aluno`.
- Criar turma persiste em `turma`.
- Vincular aluno a turma atualiza `aluno.turma_id`.
- Lancar nota persiste em `nota`.
- Aprovar retificacao atualiza nota e recalcula desempenho.
- Gerar notificacao persiste em `notificacao_responsavel`.
- Listagens e selects da web consomem endpoints REST e buscam dados do banco.
- Os dados locais permanecem no arquivo `data/acadtrack-db.mv.db` entre reinicios do backend.
