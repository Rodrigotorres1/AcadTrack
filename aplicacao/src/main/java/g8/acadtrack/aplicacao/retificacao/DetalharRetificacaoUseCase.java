package g8.acadtrack.aplicacao.retificacao;

import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class DetalharRetificacaoUseCase {

    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;
    private final MontarDetalheRetificacaoService montarDetalheRetificacaoService;

    public DetalharRetificacaoUseCase(
            SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository,
            MontarDetalheRetificacaoService montarDetalheRetificacaoService
    ) {
        this.solicitacaoRetificacaoRepository = solicitacaoRetificacaoRepository;
        this.montarDetalheRetificacaoService = montarDetalheRetificacaoService;
    }

    public SolicitacaoRetificacaoDetalheResultado executar(Long solicitacaoId) {
        SolicitacaoRetificacao solicitacao = solicitacaoRetificacaoRepository.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Solicitação de retificação não encontrada"));

        return montarDetalheRetificacaoService.montar(solicitacao);
    }
}
