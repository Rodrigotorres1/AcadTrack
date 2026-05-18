package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import g8.acadtrack.infraestrutura.persistencia.springdata.SimuladoDisciplinaSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SimuladoDisciplinaRepositoryJpa implements SimuladoDisciplinaRepository {

    private final SimuladoDisciplinaSpringDataRepository repository;

    public SimuladoDisciplinaRepositoryJpa(SimuladoDisciplinaSpringDataRepository repository) {
        this.repository = repository;
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

    @Override
    public List<SimuladoDisciplina> buscarPorSimuladoIds(List<Long> simuladoIds) {
        if (simuladoIds == null || simuladoIds.isEmpty()) {
            return List.of();
        }

        return repository.findBySimuladoIdIn(simuladoIds)
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
