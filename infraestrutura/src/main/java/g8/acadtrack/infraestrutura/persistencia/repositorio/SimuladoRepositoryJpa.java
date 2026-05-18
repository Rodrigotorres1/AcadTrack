package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.infraestrutura.persistencia.entidade.SimuladoDisciplinaJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.entidade.SimuladoJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.SimuladoDisciplinaSpringDataRepository;
import g8.acadtrack.infraestrutura.persistencia.springdata.SimuladoSpringDataRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SimuladoRepositoryJpa implements SimuladoRepository {

    private final SimuladoSpringDataRepository repository;
    private final SimuladoDisciplinaSpringDataRepository simuladoDisciplinaRepository;

    public SimuladoRepositoryJpa(
            SimuladoSpringDataRepository repository,
            SimuladoDisciplinaSpringDataRepository simuladoDisciplinaRepository
    ) {
        this.repository = repository;
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
    }

    @Override
    @Transactional
    public Simulado salvar(Simulado simulado) {
        boolean simuladoExistente = simulado.getId() != null;
        SimuladoJpaEntity entity = new SimuladoJpaEntity(
                simulado.getId(),
                simulado.getDescricao()
        );

        SimuladoJpaEntity salvo = repository.save(entity);
        List<SimuladoDisciplina> disciplinas = simulado.listarDisciplinas();

        if (simuladoExistente || !disciplinas.isEmpty()) {
            simuladoDisciplinaRepository.deleteBySimuladoId(salvo.getId());
            simuladoDisciplinaRepository.saveAll(disciplinas.stream()
                    .map(disciplina -> new SimuladoDisciplinaJpaEntity(
                            null,
                            salvo.getId(),
                            disciplina.getDisciplinaId(),
                            disciplina.getPeso()
                    ))
                    .toList());
        }

        return buscarPorId(salvo.getId()).orElseGet(() -> new Simulado(
                salvo.getId(),
                salvo.getDescricao(),
                disciplinas
        ));
    }

    @Override
    public List<Simulado> buscarTodos() {
        List<SimuladoJpaEntity> simulados = repository.findAll();
        Map<Long, List<SimuladoDisciplina>> disciplinasPorSimulado = buscarDisciplinasPorSimulado(simulados.stream()
                .map(SimuladoJpaEntity::getId)
                .toList());

        return simulados.stream()
                .map(entity -> toDomain(entity, disciplinasPorSimulado.getOrDefault(entity.getId(), List.of())))
                .toList();
    }

    @Override
    public List<Simulado> buscarPorIds(List<Long> ids) {
        Objects.requireNonNull(ids, "ids são obrigatórios");
        if (ids.isEmpty()) {
            return List.of();
        }

        List<SimuladoJpaEntity> simulados = repository.findByIdIn(ids);
        Map<Long, List<SimuladoDisciplina>> disciplinasPorSimulado = buscarDisciplinasPorSimulado(simulados.stream()
                .map(SimuladoJpaEntity::getId)
                .toList());

        return simulados.stream()
                .map(entity -> toDomain(entity, disciplinasPorSimulado.getOrDefault(entity.getId(), List.of())))
                .toList();
    }

    @Override
    public List<SimuladoDisciplina> buscarPesosDisciplinasPorSimuladoIds(List<Long> simuladoIds) {
        if (simuladoIds == null || simuladoIds.isEmpty()) {
            return List.of();
        }

        return simuladoDisciplinaRepository.findBySimuladoIdIn(simuladoIds)
                .stream()
                .map(this::toDisciplinaDomain)
                .toList();
    }

    @Override
    public Optional<Simulado> buscarPorId(Long id) {
        Long idObrigatorio = Objects.requireNonNull(id, "id é obrigatório");
        return repository.findById(idObrigatorio)
                .map(entity -> toDomain(entity, buscarDisciplinas(entity.getId())));
    }

    @Override
    public Optional<Simulado> buscarPorDescricaoNormalizada(String descricao) {
        String descricaoNormalizada = Objects.requireNonNull(descricao, "descrição é obrigatória").trim();
        return repository.findFirstByDescricaoIgnoreCase(descricaoNormalizada)
                .map(entity -> toDomain(entity, buscarDisciplinas(entity.getId())));
    }

    private Map<Long, List<SimuladoDisciplina>> buscarDisciplinasPorSimulado(List<Long> simuladoIds) {
        if (simuladoIds.isEmpty()) {
            return Map.of();
        }

        return simuladoDisciplinaRepository.findBySimuladoIdIn(simuladoIds)
                .stream()
                .map(this::toDisciplinaDomain)
                .collect(Collectors.groupingBy(SimuladoDisciplina::getSimuladoId));
    }

    private List<SimuladoDisciplina> buscarDisciplinas(Long simuladoId) {
        return simuladoDisciplinaRepository.findBySimuladoId(simuladoId)
                .stream()
                .map(this::toDisciplinaDomain)
                .toList();
    }

    private Simulado toDomain(SimuladoJpaEntity entity, List<SimuladoDisciplina> disciplinas) {
        return new Simulado(
                entity.getId(),
                entity.getDescricao(),
                disciplinas
        );
    }

    private SimuladoDisciplina toDisciplinaDomain(SimuladoDisciplinaJpaEntity entity) {
        return new SimuladoDisciplina(
                entity.getId(),
                entity.getSimuladoId(),
                entity.getDisciplinaId(),
                entity.getPeso()
        );
    }
}
