package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import br.com.acadtrack.infraestrutura.persistencia.entidade.NotaJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.NotaSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class NotaRepositoryJpa implements NotaRepository {

    private final NotaSpringDataRepository repository;

    public NotaRepositoryJpa(NotaSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public void salvar(Nota nota) {
        NotaJpaEntity entity = new NotaJpaEntity(
                nota.getId(),
                nota.getAlunoId(),
                nota.getSimuladoId(),
                nota.getDisciplina(),
                nota.getValor()
        );
        repository.save(entity);
    }

    @Override
    public Optional<Nota> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplina(),
                        entity.getValor()
                ));
    }

    @Override
    public List<Nota> buscarPorAlunoId(Long alunoId) {
        return repository.findByAlunoId(alunoId)
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplina(),
                        entity.getValor()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Nota> buscarPorSimuladoId(Long simuladoId) {
        return repository.findBySimuladoId(simuladoId)
                .stream()
                .map(entity -> new Nota(
                        entity.getId(),
                        entity.getAlunoId(),
                        entity.getSimuladoId(),
                        entity.getDisciplina(),
                        entity.getValor()
                ))
                .collect(Collectors.toList());
    }
}