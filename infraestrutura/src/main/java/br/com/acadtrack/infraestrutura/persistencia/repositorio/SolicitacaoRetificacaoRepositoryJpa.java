package br.com.acadtrack.infraestrutura.persistencia.repositorio;

import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import br.com.acadtrack.infraestrutura.persistencia.entidade.SolicitacaoRetificacaoJpaEntity;
import br.com.acadtrack.infraestrutura.persistencia.springdata.SolicitacaoRetificacaoSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SolicitacaoRetificacaoRepositoryJpa implements SolicitacaoRetificacaoRepository {

    private final SolicitacaoRetificacaoSpringDataRepository repository;

    public SolicitacaoRetificacaoRepositoryJpa(SolicitacaoRetificacaoSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public void salvar(SolicitacaoRetificacao solicitacao) {
        SolicitacaoRetificacaoJpaEntity entity = new SolicitacaoRetificacaoJpaEntity(
                solicitacao.getId(),
                solicitacao.getNotaId(),
                solicitacao.getJustificativa(),
                solicitacao.getStatus()
        );
        repository.save(entity);
    }

    @Override
    public Optional<SolicitacaoRetificacao> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> {
                    SolicitacaoRetificacao solicitacao =
                            new SolicitacaoRetificacao(entity.getId(), entity.getNotaId(), entity.getJustificativa());

                    if ("APROVADA".equals(entity.getStatus())) {
                        solicitacao.aprovar();
                    } else if ("REJEITADA".equals(entity.getStatus())) {
                        solicitacao.rejeitar();
                    }

                    return solicitacao;
                });
    }
}