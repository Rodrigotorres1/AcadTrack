package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import br.com.acadtrack.infraestrutura.persistencia.entidade.SolicitacaoRetificacaoJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.SolicitacaoRetificacaoSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SolicitacaoRetificacaoRepositoryJpa implements SolicitacaoRetificacaoRepository {

    private final SolicitacaoRetificacaoSpringDataRepository repository;

    public SolicitacaoRetificacaoRepositoryJpa(SolicitacaoRetificacaoSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public SolicitacaoRetificacao salvar(SolicitacaoRetificacao solicitacao) {
        SolicitacaoRetificacaoJpaEntity entity = new SolicitacaoRetificacaoJpaEntity(
                solicitacao.getId(),
                solicitacao.getNotaId(),
                solicitacao.getJustificativa(),
                solicitacao.getStatus()
        );

        SolicitacaoRetificacaoJpaEntity salva = repository.save(entity);

        return new SolicitacaoRetificacao(
                salva.getId(),
                salva.getNotaId(),
                salva.getJustificativa(),
                salva.getStatus()
        );
    }

    @Override
    public Optional<SolicitacaoRetificacao> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new SolicitacaoRetificacao(
                        entity.getId(),
                        entity.getNotaId(),
                        entity.getJustificativa(),
                        entity.getStatus()
                ));
    }

    @Override
    public List<SolicitacaoRetificacao> buscarTodas() {
        return repository.findAll()
                .stream()
                .map(entity -> new SolicitacaoRetificacao(
                        entity.getId(),
                        entity.getNotaId(),
                        entity.getJustificativa(),
                        entity.getStatus()
                ))
                .toList();
    }
}