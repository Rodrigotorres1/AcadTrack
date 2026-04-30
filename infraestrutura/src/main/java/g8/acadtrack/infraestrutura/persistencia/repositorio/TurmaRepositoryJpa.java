package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominioacademico.turma.Turma;
import g8.acadtrack.dominioacademico.turma.TurmaRepository;
import g8.acadtrack.infraestrutura.persistencia.entidade.TurmaJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.TurmaSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TurmaRepositoryJpa implements TurmaRepository {

    private final TurmaSpringDataRepository repository;

    public TurmaRepositoryJpa(TurmaSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Turma salvar(Turma turma) {
        TurmaJpaEntity entity = new TurmaJpaEntity(
                turma.getId(),
                turma.getNome()
        );

        TurmaJpaEntity salva = repository.save(entity);

        return new Turma(
                salva.getId(),
                salva.getNome()
        );
    }

    @Override
    public Optional<Turma> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Turma(
                        entity.getId(),
                        entity.getNome()
                ));
    }
}