package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import br.com.acadtrack.dominioacademico.disciplina.StatusDisciplina;
import br.com.acadtrack.infraestrutura.persistencia.entidade.DisciplinaJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.NotaSpringDataRepository;
import br.com.acadtrack.infraestrutura.persistencia.springdata.DisciplinaSpringDataRepository;
import br.com.acadtrack.infraestrutura.persistencia.springdata.SimuladoDisciplinaSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class DisciplinaRepositoryJpa implements DisciplinaRepository {

    private final DisciplinaSpringDataRepository repository;
    private final NotaSpringDataRepository notaSpringDataRepository;
    private final SimuladoDisciplinaSpringDataRepository simuladoDisciplinaSpringDataRepository;

    public DisciplinaRepositoryJpa(
            DisciplinaSpringDataRepository repository,
            NotaSpringDataRepository notaSpringDataRepository,
            SimuladoDisciplinaSpringDataRepository simuladoDisciplinaSpringDataRepository
    ) {
        this.repository = repository;
        this.notaSpringDataRepository = notaSpringDataRepository;
        this.simuladoDisciplinaSpringDataRepository = simuladoDisciplinaSpringDataRepository;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        DisciplinaJpaEntity entity = new DisciplinaJpaEntity(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getStatus().name()
        );

        DisciplinaJpaEntity salva = repository.save(entity);

        return toDomain(salva);
    }

    @Override
    public List<Disciplina> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Disciplina> buscarPorId(Long id) {
        Objects.requireNonNull(id, "id é obrigatório");
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Disciplina> buscarPorNomeNormalizado(String nomeNormalizado) {
        String nome = Disciplina.normalizarNome(nomeNormalizado);
        return repository.findFirstByNomeIgnoreCase(nome).map(this::toDomain);
    }

    @Override
    public List<Disciplina> buscarPorIds(List<Long> ids) {
        Objects.requireNonNull(ids, "ids são obrigatórios");
        return repository.findAllById(ids)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean possuiVinculoAcademico(Long disciplinaId) {
        return notaSpringDataRepository.existsByDisciplinaId(disciplinaId)
                || simuladoDisciplinaSpringDataRepository.existsByDisciplinaId(disciplinaId);
    }

    private Disciplina toDomain(DisciplinaJpaEntity entity) {
        StatusDisciplina status = entity.getStatus() == null
                ? StatusDisciplina.ATIVA
                : StatusDisciplina.valueOf(entity.getStatus());
        return new Disciplina(entity.getId(), entity.getNome(), status);
    }
}
