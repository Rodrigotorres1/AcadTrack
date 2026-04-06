package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import br.com.acadtrack.infraestrutura.persistencia.entidade.SimuladoDisciplinaJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.SimuladoDisciplinaSpringDataRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class SimuladoDisciplinaRepositoryJpa implements SimuladoDisciplinaRepository {

    private final SimuladoDisciplinaSpringDataRepository repository;

    public SimuladoDisciplinaRepositoryJpa(SimuladoDisciplinaSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public SimuladoDisciplina salvar(SimuladoDisciplina simuladoDisciplina) {
        SimuladoDisciplinaJpaEntity entity = new SimuladoDisciplinaJpaEntity(
                simuladoDisciplina.getId(),
                simuladoDisciplina.getSimuladoId(),
                simuladoDisciplina.getDisciplinaId(),
                simuladoDisciplina.getPeso()
        );

        SimuladoDisciplinaJpaEntity salva = repository.save(entity);

        return new SimuladoDisciplina(
                salva.getId(),
                salva.getSimuladoId(),
                salva.getDisciplinaId(),
                salva.getPeso()
        );
    }

    @Override
public List<SimuladoDisciplina> buscarPorSimulado(Long simuladoId) {

    return repository.findBySimuladoId(simuladoId)
            .stream()
            .map(entity -> new SimuladoDisciplina(
                    entity.getId(),
                    entity.getSimuladoId(),
                    entity.getDisciplinaId(),
                    entity.getPeso()
            ))
            .toList();
}
}