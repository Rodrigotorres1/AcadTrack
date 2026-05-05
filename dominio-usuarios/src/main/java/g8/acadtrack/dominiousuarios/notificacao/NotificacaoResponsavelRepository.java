package g8.acadtrack.dominiousuarios.notificacao;

import java.util.List;

public interface NotificacaoResponsavelRepository {

    NotificacaoResponsavel salvar(NotificacaoResponsavel notificacao);

    List<NotificacaoResponsavel> buscarPorResponsavelId(Long responsavelId);
}
