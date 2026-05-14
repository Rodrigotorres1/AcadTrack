package g8.acadtrack.dominiousuarios.notificacao;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;

import java.util.List;
import java.util.Optional;

public interface NotificacaoResponsavelRepository {

    NotificacaoResponsavel salvar(NotificacaoResponsavel notificacao);

    Optional<NotificacaoResponsavel> buscarPorId(Long id);

    List<NotificacaoResponsavel> buscarPorResponsavelId(Long responsavelId);

    boolean existeNotificacaoNaoLidaPara(Long alunoId, Long responsavelId, NivelRiscoAcademico nivelRisco);
}
