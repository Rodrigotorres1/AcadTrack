package br.com.acadtrack.aplicacao.retificacao;

import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import br.com.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class IniciarAnaliseRetificacaoUseCase {

    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;

    public IniciarAnaliseRetificacaoUseCase(SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository) {
        this.solicitacaoRetificacaoRepository = solicitacaoRetificacaoRepository;
    }

    public SolicitacaoRetificacao executar(Long solicitacaoId) {
        SolicitacaoRetificacao solicitacao = solicitacaoRetificacaoRepository.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Solicitação de retificação não encontrada"));

        solicitacao.iniciarAnalise();
        return solicitacaoRetificacaoRepository.salvar(solicitacao);
    }
}
