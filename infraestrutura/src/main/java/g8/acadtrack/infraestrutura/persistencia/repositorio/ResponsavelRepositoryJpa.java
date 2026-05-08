package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominiousuarios.responsavel.Responsavel;
import g8.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import g8.acadtrack.infraestrutura.persistencia.entidade.ResponsavelJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.ResponsavelSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ResponsavelRepositoryJpa implements ResponsavelRepository {

    private final ResponsavelSpringDataRepository repository;

    public ResponsavelRepositoryJpa(ResponsavelSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Responsavel salvar(Responsavel responsavel) {
        ResponsavelJpaEntity entity = new ResponsavelJpaEntity(
                responsavel.getId(),
                responsavel.getNome(),
                responsavel.getEmail()
        );

        ResponsavelJpaEntity salvo = repository.save(entity);

        return new Responsavel(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail()
        );
    }

    @Override
    public List<Responsavel> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(entity -> new Responsavel(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail()
                ))
                .toList();
    }

    @Override
    public Optional<Responsavel> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Responsavel(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail()
                ));
    }

    @Override
    public void excluirPorId(Long id) {
        repository.deleteById(id);
    }
}
