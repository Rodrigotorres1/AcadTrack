package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import br.com.acadtrack.infraestrutura.persistencia.entidade.DisciplinaJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.DisciplinaSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DisciplinaRepositoryJpa implements DisciplinaRepository {

    private final DisciplinaSpringDataRepository repository;

    public DisciplinaRepositoryJpa(DisciplinaSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        DisciplinaJpaEntity entity = new DisciplinaJpaEntity(
                disciplina.getId(),
                disciplina.getNome()
        );

        DisciplinaJpaEntity salva = repository.save(entity);

        return new Disciplina(
                salva.getId(),
                salva.getNome()
        );
    }

    @Override
    public Optional<Disciplina> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Disciplina(
                        entity.getId(),
                        entity.getNome()
                ));
    }
}