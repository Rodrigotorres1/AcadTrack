package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioacademico.turma.Turma;
import br.com.acadtrack.dominioacademico.turma.TurmaRepository;
import br.com.acadtrack.infraestrutura.persistencia.entidade.TurmaJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.TurmaSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TurmaRepositoryJpa implements TurmaRepository {

    private final TurmaSpringDataRepository repository;

    public TurmaRepositoryJpa(TurmaSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public void salvar(Turma turma) {
        TurmaJpaEntity entity = new TurmaJpaEntity(
                turma.getId(),
                turma.getNome()
        );
        repository.save(entity);
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