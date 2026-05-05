package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavel;
import g8.acadtrack.dominiousuarios.notificacao.PrioridadeNotificacao;
import g8.acadtrack.dominiousuarios.notificacao.StatusNotificacao;

import java.time.LocalDateTime;

public record NotificacaoResponsavelResponse(
        Long id,
        Long alunoId,
        Long responsavelId,
        String nivelRisco,
        PrioridadeNotificacao prioridade,
        String mensagem,
        LocalDateTime dataCriacao,
        StatusNotificacao status
) {
    public static NotificacaoResponsavelResponse fromDomain(NotificacaoResponsavel notificacao) {
        return new NotificacaoResponsavelResponse(
                notificacao.getId(),
                notificacao.getAlunoId(),
                notificacao.getResponsavelId(),
                notificacao.getNivelRisco(),
                notificacao.getPrioridade(),
                notificacao.getMensagem(),
                notificacao.getDataCriacao(),
                notificacao.getStatus()
        );
    }
}
