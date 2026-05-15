package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.infraestrutura.persistencia.entidade.AlunoJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.AlunoSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AlunoRepositoryJpa implements AlunoRepository {

    private final AlunoSpringDataRepository repository;

    public AlunoRepositoryJpa(AlunoSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        AlunoJpaEntity entity = new AlunoJpaEntity(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTurmaId(),
                aluno.getResponsavelId(),
                aluno.isVinculoResponsavelAtivo(),
                aluno.isPermissaoVisualizarNotas(),
                aluno.isPermissaoVisualizarSimulados(),
                aluno.isPermissaoVisualizarDesempenho(),
                aluno.isAtivo(),
                aluno.getMediaAritmetica(),
                aluno.getSituacaoAcademica()
        );

        AlunoJpaEntity salvo = repository.save(entity);

        return new Aluno(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail(),
                salvo.getTurmaId(),
                salvo.getResponsavelId(),
                salvo.isVinculoResponsavelAtivo(),
                salvo.isPermissaoVisualizarNotas(),
                salvo.isPermissaoVisualizarSimulados(),
                salvo.isPermissaoVisualizarDesempenho(),
                salvo.isAtivo(),
                salvo.getMediaAritmetica(),
                salvo.getSituacaoAcademica()
        );
    }

    @Override
    public boolean existeAlunoComEmailIgnorandoMaiusculas(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return repository.existsByEmailIgnoreCase(email.trim());
    }

    @Override
    public Optional<Aluno> buscarPorId(Long id) {
        Objects.requireNonNull(id, "id é obrigatório");
        return repository.findById(id)
                .map(entity -> new Aluno(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail(),
                        entity.getTurmaId(),
                        entity.getResponsavelId(),
                        entity.isVinculoResponsavelAtivo(),
                        entity.isPermissaoVisualizarNotas(),
                        entity.isPermissaoVisualizarSimulados(),
                        entity.isPermissaoVisualizarDesempenho(),
                        entity.isAtivo(),
                        entity.getMediaAritmetica(),
                        entity.getSituacaoAcademica()
                ));
    }

    @Override
    public List<Aluno> buscarPorIds(List<Long> ids) {
        Objects.requireNonNull(ids, "ids são obrigatórios");
        if (ids.isEmpty()) {
            return List.of();
        }

        return repository.findByIdIn(ids)
                .stream()
                .map(entity -> new Aluno(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail(),
                        entity.getTurmaId(),
                        entity.getResponsavelId(),
                        entity.isVinculoResponsavelAtivo(),
                        entity.isPermissaoVisualizarNotas(),
                        entity.isPermissaoVisualizarSimulados(),
                        entity.isPermissaoVisualizarDesempenho(),
                        entity.isAtivo(),
                        entity.getMediaAritmetica(),
                        entity.getSituacaoAcademica()
                ))
                .toList();
    }

    @Override
    public List<Aluno> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(entity -> new Aluno(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail(),
                        entity.getTurmaId(),
                        entity.getResponsavelId(),
                        entity.isVinculoResponsavelAtivo(),
                        entity.isPermissaoVisualizarNotas(),
                        entity.isPermissaoVisualizarSimulados(),
                        entity.isPermissaoVisualizarDesempenho(),
                        entity.isAtivo(),
                        entity.getMediaAritmetica(),
                        entity.getSituacaoAcademica()
                ))
                .toList();
    }

    @Override
    public List<Aluno> buscarPorResponsavelId(Long responsavelId) {
        Long responsavelIdObrigatorio = Objects.requireNonNull(responsavelId, "responsavelId é obrigatório");
        return repository.findByResponsavelId(responsavelIdObrigatorio)
                .stream()
                .map(entity -> new Aluno(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail(),
                        entity.getTurmaId(),
                        entity.getResponsavelId(),
                        entity.isVinculoResponsavelAtivo(),
                        entity.isPermissaoVisualizarNotas(),
                        entity.isPermissaoVisualizarSimulados(),
                        entity.isPermissaoVisualizarDesempenho(),
                        entity.isAtivo(),
                        entity.getMediaAritmetica(),
                        entity.getSituacaoAcademica()
                ))
                .toList();
    }

}
