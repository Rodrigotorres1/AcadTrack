package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.infraestrutura.persistencia.entidade.AlunoJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.AlunoSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AlunoRepositoryJpa implements AlunoRepository {

    private final AlunoSpringDataRepository repository;

    public AlunoRepositoryJpa(AlunoSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        AlunoJpaEntity entity = new AlunoJpaEntity(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail()
        );

        AlunoJpaEntity salvo = repository.save(entity);

        return new Aluno(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail()
        );
    }

    @Override
    public Optional<Aluno> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Aluno(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail()
                ));
    }
}