package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import br.com.acadtrack.infraestrutura.persistencia.entidade.SimuladoJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.SimuladoSpringDataRepository;
import org.springframework.stereotype.Repository;

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
    public Optional<Simulado> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Simulado(
                        entity.getId(),
                        entity.getDescricao()
                ));
    }
}