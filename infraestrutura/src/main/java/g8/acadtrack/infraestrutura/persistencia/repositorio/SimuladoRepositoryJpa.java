package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.infraestrutura.persistencia.entidade.SimuladoJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.SimuladoSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class SimuladoRepositoryJpa implements SimuladoRepository {

    private final SimuladoSpringDataRepository repository;

    public SimuladoRepositoryJpa(SimuladoSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Simulado salvar(Simulado simulado) {
        SimuladoJpaEntity entity = new SimuladoJpaEntity(
                simulado.getId(),
                simulado.getDescricao()
        );

        SimuladoJpaEntity salvo = repository.save(entity);

        return new Simulado(
                salvo.getId(),
                salvo.getDescricao()
        );
    }

    @Override
    public List<Simulado> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(entity -> new Simulado(
                        entity.getId(),
                        entity.getDescricao()
                ))
                .toList();
    }

    @Override
    public List<Simulado> buscarPorIds(List<Long> ids) {
        Objects.requireNonNull(ids, "ids são obrigatórios");
        if (ids.isEmpty()) {
            return List.of();
        }

        return repository.findByIdIn(ids)
                .stream()
                .map(entity -> new Simulado(
                        entity.getId(),
                        entity.getDescricao()
                ))
                .toList();
    }

    @Override
    public Optional<Simulado> buscarPorId(Long id) {
        Long idObrigatorio = Objects.requireNonNull(id, "id é obrigatório");
        return repository.findById(idObrigatorio)
                .map(entity -> new Simulado(
                        entity.getId(),
                        entity.getDescricao()
                ));
    }

    @Override
    public Optional<Simulado> buscarPorDescricaoNormalizada(String descricao) {
        String descricaoNormalizada = Objects.requireNonNull(descricao, "descrição é obrigatória").trim();
        return repository.findFirstByDescricaoIgnoreCase(descricaoNormalizada)
                .map(entity -> new Simulado(
                        entity.getId(),
                        entity.getDescricao()
                ));
    }
}
