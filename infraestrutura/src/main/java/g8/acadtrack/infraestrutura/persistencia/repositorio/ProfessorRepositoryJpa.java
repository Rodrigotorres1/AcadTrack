package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominiousuarios.professor.Professor;
import g8.acadtrack.dominiousuarios.professor.ProfessorRepository;
import g8.acadtrack.infraestrutura.persistencia.entidade.ProfessorJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.ProfessorSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProfessorRepositoryJpa implements ProfessorRepository {

    private final ProfessorSpringDataRepository repository;

    public ProfessorRepositoryJpa(ProfessorSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Professor salvar(Professor professor) {
        ProfessorJpaEntity entity = new ProfessorJpaEntity(
                professor.getId(),
                professor.getNome(),
                professor.getEmail()
        );

        ProfessorJpaEntity salvo = repository.save(entity);

        return new Professor(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail()
        );
    }

    @Override
    public Optional<Professor> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Professor(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail()
                ));
    }
}