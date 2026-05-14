package g8.acadtrack.aplicacao.retificacao;

import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import g8.acadtrack.dominioavaliacao.retificacao.StatusSolicitacaoRetificacao;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitarRetificacaoUseCase {

    private final NotaRepository notaRepository;
    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;

    public SolicitarRetificacaoUseCase(
            NotaRepository notaRepository,
            SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository
    ) {
        this.notaRepository = notaRepository;
        this.solicitacaoRetificacaoRepository = solicitacaoRetificacaoRepository;
    }

    @Transactional
    public SolicitacaoRetificacao executar(Long notaId, String justificativa) {
        notaRepository.buscarPorId(notaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Nota não encontrada"));
    

        SolicitacaoRetificacao solicitacao = new SolicitacaoRetificacao(
                null,
                notaId,
                justificativa,
                null,
                StatusSolicitacaoRetificacao.PENDENTE
        );

        if (solicitacaoRetificacaoRepository.existeEmAbertoPorNotaId(notaId)) {
            throw new ConflitoDeEstadoException("Já existe solicitação de retificação em aberto para esta nota");
        }

        return solicitacaoRetificacaoRepository.salvar(solicitacao);
    }
}
