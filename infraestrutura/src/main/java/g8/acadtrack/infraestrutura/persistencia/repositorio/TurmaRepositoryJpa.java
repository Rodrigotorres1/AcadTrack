package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominioacademico.turma.Turma;
import g8.acadtrack.dominioacademico.turma.TurmaRepository;
import g8.acadtrack.infraestrutura.persistencia.entidade.TurmaJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.TurmaSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public List<Turma> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(entity -> new Turma(
                        entity.getId(),
                        entity.getNome()
                ))
                .toList();
    }

    @Override
    public Optional<Turma> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Turma(
                        entity.getId(),
                        entity.getNome()
                ));
    }

    @Override
    public boolean existeComNomeNormalizado(String nomeNormalizado) {
        return repository.findAll()
                .stream()
                .map(TurmaJpaEntity::getNome)
                .map(Turma::normalizarNome)
                .anyMatch(nomeNormalizado::equals);
    }

    @Override
    public void excluirPorId(Long id) {
        repository.deleteById(id);
    }
}
