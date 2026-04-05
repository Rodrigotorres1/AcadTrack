package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominiousuarios.responsavel.Responsavel;
import br.com.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import br.com.acadtrack.infraestrutura.persistencia.entidade.ResponsavelJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.ResponsavelSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ResponsavelRepositoryJpa implements ResponsavelRepository {

    private final ResponsavelSpringDataRepository repository;

    public ResponsavelRepositoryJpa(ResponsavelSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public void salvar(Responsavel responsavel) {
        ResponsavelJpaEntity entity = new ResponsavelJpaEntity(
                responsavel.getId(),
                responsavel.getNome(),
                responsavel.getEmail(),
                null
        );
        repository.save(entity);
    }

    @Override
    public Optional<Responsavel> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Responsavel(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail()
                ));
    }

    @Override
    public Optional<Responsavel> buscarPorAlunoId(Long alunoId) {
        return repository.findByAlunoId(alunoId)
                .map(entity -> new Responsavel(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail()
                ));
    }

    @Override
    public void vincularAoAluno(Long responsavelId, Long alunoId) {
        ResponsavelJpaEntity entity = repository.findById(responsavelId)
                .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));

        entity.setAlunoId(alunoId);
        repository.save(entity);
    }

    @Override
    public void desvincularDoAluno(Long alunoId) {
        ResponsavelJpaEntity entity = repository.findByAlunoId(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado para o aluno"));

        entity.setAlunoId(null);
        repository.save(entity);
    }
}