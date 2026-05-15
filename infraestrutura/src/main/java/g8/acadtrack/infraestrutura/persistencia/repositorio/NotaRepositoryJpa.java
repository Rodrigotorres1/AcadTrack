package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.aplicacao.ranking.ContadorParticipantesRankingPort;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.infraestrutura.persistencia.entidade.AlunoJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.entidade.NotaJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.NotaSpringDataRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class NotaRepositoryJpa implements NotaRepository, ContadorParticipantesRankingPort {

    private final NotaSpringDataRepository repository;
    private final EntityManager entityManager;

    public NotaRepositoryJpa(
            NotaSpringDataRepository repository,
            EntityManager entityManager
    ) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public Nota salvar(Nota nota) {
        NotaJpaEntity entity = new NotaJpaEntity(
                nota.getId(),
                nota.getAlunoId(),
                nota.getSimuladoId(),
                nota.getDisciplinaId(),
                nota.getValor()
        );

        NotaJpaEntity salva = repository.save(entity);

        return new Nota(
                salva.getId(), 
                salva.getAlunoId(),
                salva.getSimuladoId(),
                salva.getDisciplinaId(),
                salva.getValor()
        );
    }

    @Override
    public List<Nota> buscarPorAlunoESimulado(Long alunoId, Long simuladoId) {
        return repository.findByAlunoIdAndSimuladoId(alunoId, simuladoId)
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ))
                .toList();
    }

    @Override
    public List<Nota> buscarPorAlunoId(Long alunoId) {
        return repository.findByAlunoId(alunoId)
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ))
                .toList();
    }

    @Override
    public List<Nota> buscarPorAlunoIds(List<Long> alunoIds) {
        if (alunoIds == null || alunoIds.isEmpty()) {
            return List.of();
        }

        return repository.findByAlunoIdIn(alunoIds)
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ))
                .toList();
    }

    @Override
    public List<Nota> buscarPorSimuladoId(Long simuladoId) {
        return repository.findBySimuladoId(simuladoId)
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ))
                .toList();
    }

    @Override
    public Optional<Nota> buscarPorId(Long id) {
        Long idObrigatorio = Objects.requireNonNull(id, "id é obrigatório");
        return repository.findById(idObrigatorio)
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ));
    }

    @Override
    public List<Nota> buscarPorIds(List<Long> ids) {
        Objects.requireNonNull(ids, "ids são obrigatórios");
        if (ids.isEmpty()) {
            return List.of();
        }

        return repository.findByIdIn(ids)
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ))
                .toList();
    }

    @Override
    public List<Nota> buscarTodas() {
        return repository.findAll()
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ))
                .toList();
    }

    @Override
    public List<Aluno> buscarParticipantes() {
        return entityManager.createQuery(
                        """
                                select aluno
                                from AlunoJpaEntity aluno
                                where exists (
                                    select nota.id
                                    from NotaJpaEntity nota
                                    where nota.alunoId = aluno.id
                                )
                                """,
                        AlunoJpaEntity.class
                )
                .getResultList()
                .stream()
                .map(this::toAlunoDomain)
                .toList();
    }

    @Override
    public long contarParticipantes() {
        return entityManager.createQuery(
                        "select count(distinct nota.alunoId) from NotaJpaEntity nota",
                        Long.class
                )
                .getSingleResult();
    }

    @Override
    public long contarParticipantesComMediaMaiorQue(double mediaGeral) {
        return entityManager.createQuery(
                        """
                                select nota.alunoId
                                from NotaJpaEntity nota
                                group by nota.alunoId
                                having avg(nota.valor) > :mediaGeral
                                """,
                        Long.class
                )
                .setParameter("mediaGeral", mediaGeral)
                .getResultList()
                .size();
    }

    @Override
    public boolean existePorAlunoSimuladoDisciplina(Long alunoId, Long simuladoId, Long disciplinaId) {
        Long alunoIdObrigatorio = Objects.requireNonNull(alunoId, "alunoId é obrigatório");
        Long simuladoIdObrigatorio = Objects.requireNonNull(simuladoId, "simuladoId é obrigatório");
        Long disciplinaIdObrigatorio = Objects.requireNonNull(disciplinaId, "disciplinaId é obrigatório");
        return repository.existsByAlunoIdAndSimuladoIdAndDisciplinaId(
                alunoIdObrigatorio,
                simuladoIdObrigatorio,
                disciplinaIdObrigatorio
        );
    }

    private Aluno toAlunoDomain(AlunoJpaEntity entity) {
        return new Aluno(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTurmaId(),
                entity.getResponsavelId(),
                entity.isVinculoResponsavelAtivo(),
                entity.isPermissaoVisualizarNotas(),
                entity.isPermissaoVisualizarSimulados(),
                entity.isPermissaoVisualizarDesempenho(),
                entity.isAtivo(),
                entity.getMediaAritmetica(),
                entity.getSituacaoAcademica()
        );
    }
}
