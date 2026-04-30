package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import g8.acadtrack.infraestrutura.persistencia.entidade.SolicitacaoRetificacaoJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.SolicitacaoRetificacaoSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
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
                solicitacao.getJustificativaDecisao(),
                solicitacao.getStatus()
        );

        SolicitacaoRetificacaoJpaEntity salva = repository.save(entity);

        return new SolicitacaoRetificacao(
                salva.getId(),
                salva.getNotaId(),
                salva.getJustificativa(),
                salva.getJustificativaDecisao(),
                salva.getStatus()
        );
    }

    @Override
    public Optional<SolicitacaoRetificacao> buscarPorId(Long id) {
        Long idObrigatorio = Objects.requireNonNull(id, "id é obrigatório");
        return repository.findById(idObrigatorio)
                .map(entity -> new SolicitacaoRetificacao(
                        entity.getId(),
                        entity.getNotaId(),
                        entity.getJustificativa(),
                        entity.getJustificativaDecisao(),
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
                        entity.getJustificativaDecisao(),
                        entity.getStatus()
                ))
                .toList();
    }

    @Override
    public boolean existeEmAbertoPorNotaId(Long notaId) {
        Long notaIdObrigatorio = Objects.requireNonNull(notaId, "notaId é obrigatório");
        return repository.existsByNotaIdAndStatusIn(
                notaIdObrigatorio,
                List.of(
                        SolicitacaoRetificacao.STATUS_PENDENTE,
                        SolicitacaoRetificacao.STATUS_EM_ANALISE
                )
        );
    }

    @Override
    public List<SolicitacaoRetificacao> buscarPorNotaId(Long notaId) {
        Long notaIdObrigatorio = Objects.requireNonNull(notaId, "notaId é obrigatório");
        return repository.findByNotaId(notaIdObrigatorio)
                .stream()
                .map(entity -> new SolicitacaoRetificacao(
                        entity.getId(),
                        entity.getNotaId(),
                        entity.getJustificativa(),
                        entity.getJustificativaDecisao(),
                        entity.getStatus()
                ))
                .toList();
    }
}