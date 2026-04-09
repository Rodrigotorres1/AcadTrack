package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import br.com.acadtrack.infraestrutura.persistencia.entidade.NotaJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.NotaSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotaRepositoryJpa implements NotaRepository {

    private final NotaSpringDataRepository repository;

    public NotaRepositoryJpa(NotaSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Nota salvar(Nota nota) {
        NotaJpaEntity entity = new NotaJpaEntity(
                null,
                nota.getAlunoId(),
                nota.getSimuladoId(),
                nota.getDisciplinaId(),
                nota.getValor()
        );

        NotaJpaEntity salva = repository.save(entity);

        return new Nota(
                salva.getId(), 
                salva.getAlunoId(),
                salva.getSimuladoId(),
                salva.getDisciplinaId(),
                salva.getValor()
        );
    }

    @Override
    public List<Nota> buscarPorAlunoESimulado(Long alunoId, Long simuladoId) {
        return repository.findByAlunoIdAndSimuladoId(alunoId, simuladoId)
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ))
                .toList();
    }

    @Override
    public List<Nota> buscarPorAlunoId(Long alunoId) {
        return repository.findByAlunoId(alunoId)
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ))
                .toList();
    }

    @Override
    public List<Nota> buscarPorSimuladoId(Long simuladoId) {
        return repository.findBySimuladoId(simuladoId)
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ))
                .toList();
    }

    @Override
    public Optional<Nota> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplinaId(),
                        entity.getValor()
                ));
    }

    @Override
    public List<Nota> buscarTodas() {
        throw new UnsupportedOperationException("Unimplemented method 'buscarTodas'");
    }
}