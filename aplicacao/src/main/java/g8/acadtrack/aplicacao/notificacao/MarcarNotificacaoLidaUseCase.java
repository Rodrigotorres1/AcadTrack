package g8.acadtrack.aplicacao.notificacao;

import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavel;
import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavelRepository;
import g8.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarcarNotificacaoLidaUseCase {

    private final ResponsavelRepository responsavelRepository;
    private final NotificacaoResponsavelRepository notificacaoResponsavelRepository;

    public MarcarNotificacaoLidaUseCase(
            ResponsavelRepository responsavelRepository,
            NotificacaoResponsavelRepository notificacaoResponsavelRepository
    ) {
        this.responsavelRepository = responsavelRepository;
        this.notificacaoResponsavelRepository = notificacaoResponsavelRepository;
    }

    @Transactional
    public NotificacaoResponsavel executar(Long responsavelId, Long notificacaoId) {
        responsavelRepository.buscarPorId(responsavelId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Responsável não encontrado"));

        NotificacaoResponsavel notificacao = notificacaoResponsavelRepository.buscarPorId(notificacaoId)
                .filter(item -> responsavelId.equals(item.getResponsavelId()))
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Notificação não encontrada"));

        notificacao.marcarLida();
        return notificacaoResponsavelRepository.salvar(notificacao);
    }
}
