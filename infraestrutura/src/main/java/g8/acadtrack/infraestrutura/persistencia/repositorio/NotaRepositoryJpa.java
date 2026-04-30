package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.infraestrutura.persistencia.entidade.NotaJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.NotaSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
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
                nota.getId(),
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
        Long idObrigatorio = Objects.requireNonNull(id, "id é obrigatório");
        return repository.findById(idObrigatorio)
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
        return repository.findAll()
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
    public boolean existePorAlunoSimuladoDisciplina(Long alunoId, Long simuladoId, Long disciplinaId) {
        Long alunoIdObrigatorio = Objects.requireNonNull(alunoId, "alunoId é obrigatório");
        Long simuladoIdObrigatorio = Objects.requireNonNull(simuladoId, "simuladoId é obrigatório");
        Long disciplinaIdObrigatorio = Objects.requireNonNull(disciplinaId, "disciplinaId é obrigatório");
        return repository.existsByAlunoIdAndSimuladoIdAndDisciplinaId(
                alunoIdObrigatorio,
                simuladoIdObrigatorio,
                disciplinaIdObrigatorio
        );
    }
}