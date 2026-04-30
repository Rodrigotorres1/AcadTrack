package g8.acadtrack.aplicacao.retificacao;

import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class ReprovarRetificacaoUseCase {

    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;

    public ReprovarRetificacaoUseCase(SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository) {
        this.solicitacaoRetificacaoRepository = solicitacaoRetificacaoRepository;
    }

    public SolicitacaoRetificacao executar(Long solicitacaoId, String justificativaDecisao) {
        SolicitacaoRetificacao solicitacao = solicitacaoRetificacaoRepository.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Solicitação de retificação não encontrada"));

        solicitacao.reprovar(justificativaDecisao);
        return solicitacaoRetificacaoRepository.salvar(solicitacao);
    }
}
