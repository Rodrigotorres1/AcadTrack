package g8.acadtrack.dominiousuarios.notificacao;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;

import java.util.List;

public interface NotificacaoResponsavelRepository {

    NotificacaoResponsavel salvar(NotificacaoResponsavel notificacao);

    List<NotificacaoResponsavel> buscarPorResponsavelId(Long responsavelId);

    boolean existeNotificacaoNaoLidaPara(Long alunoId, Long responsavelId, NivelRiscoAcademico nivelRisco);
}
