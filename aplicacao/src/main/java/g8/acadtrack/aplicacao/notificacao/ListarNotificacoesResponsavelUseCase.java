package g8.acadtrack.aplicacao.notificacao;

import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavel;
import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavelRepository;
import g8.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarNotificacoesResponsavelUseCase {

    private final ResponsavelRepository responsavelRepository;
    private final NotificacaoResponsavelRepository notificacaoResponsavelRepository;

    public ListarNotificacoesResponsavelUseCase(
            ResponsavelRepository responsavelRepository,
            NotificacaoResponsavelRepository notificacaoResponsavelRepository
    ) {
        this.responsavelRepository = responsavelRepository;
        this.notificacaoResponsavelRepository = notificacaoResponsavelRepository;
    }

    public List<NotificacaoResponsavel> executar(Long responsavelId) {
        responsavelRepository.buscarPorId(responsavelId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Responsavel nao encontrado"));
        return notificacaoResponsavelRepository.buscarPorResponsavelId(responsavelId);
    }
}
