package g8.acadtrack.infraestrutura.persistencia.repositorio;

import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavel;
import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavelRepository;
import g8.acadtrack.dominiousuarios.notificacao.PrioridadeNotificacao;
import g8.acadtrack.dominiousuarios.notificacao.StatusNotificacao;
import g8.acadtrack.infraestrutura.persistencia.entidade.NotificacaoResponsavelJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.springdata.NotificacaoResponsavelSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class NotificacaoResponsavelRepositoryJpa implements NotificacaoResponsavelRepository {

    private final NotificacaoResponsavelSpringDataRepository repository;

    public NotificacaoResponsavelRepositoryJpa(NotificacaoResponsavelSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public NotificacaoResponsavel salvar(NotificacaoResponsavel notificacao) {
        NotificacaoResponsavelJpaEntity entity = new NotificacaoResponsavelJpaEntity(
                notificacao.getId(),
                notificacao.getAlunoId(),
                notificacao.getResponsavelId(),
                notificacao.getNivelRisco(),
                notificacao.getPrioridade().name(),
                notificacao.getMensagem(),
                notificacao.getDataCriacao(),
                notificacao.getStatus().name()
        );

        return mapear(repository.save(entity));
    }

    @Override
    public boolean existeNotificacaoNaoLidaPara(Long alunoId, Long responsavelId, String nivelRisco) {
        return repository.existsByAlunoIdAndResponsavelIdAndNivelRiscoAndStatus(
                alunoId, responsavelId, nivelRisco, StatusNotificacao.NAO_LIDA.name());
    }

    @Override
    public List<NotificacaoResponsavel> buscarPorResponsavelId(Long responsavelId) {
        Long responsavelIdObrigatorio = Objects.requireNonNull(responsavelId, "responsavelId e obrigatorio");
        return repository.findByResponsavelIdOrderByDataCriacaoDesc(responsavelIdObrigatorio)
                .stream()
                .map(this::mapear)
                .toList();
    }

    private NotificacaoResponsavel mapear(NotificacaoResponsavelJpaEntity entity) {
        return new NotificacaoResponsavel(
                entity.getId(),
                entity.getAlunoId(),
                entity.getResponsavelId(),
                entity.getNivelRisco(),
                PrioridadeNotificacao.valueOf(entity.getPrioridade()),
                entity.getMensagem(),
                entity.getDataCriacao(),
                StatusNotificacao.valueOf(entity.getStatus())
        );
    }
}
