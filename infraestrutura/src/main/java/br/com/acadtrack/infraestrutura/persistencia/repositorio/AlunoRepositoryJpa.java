package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import br.com.acadtrack.infraestrutura.persistencia.entidade.AlunoJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.AlunoSpringDataRepository;
import org.springframework.stereotype.Repository;

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
                aluno.getMediaAtual(),
                aluno.getSituacaoAcademica().name()
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
                salvo.getMediaAtual(),
                mapearSituacaoAcademica(salvo.getSituacaoAcademica())
        );
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
                        entity.getMediaAtual(),
                        mapearSituacaoAcademica(entity.getSituacaoAcademica())
                ));
    }

    private SituacaoAcademica mapearSituacaoAcademica(String situacao) {
        if (situacao == null || situacao.isBlank()) {
            return SituacaoAcademica.REPROVADO;
        }
        return SituacaoAcademica.valueOf(situacao);
    }
}