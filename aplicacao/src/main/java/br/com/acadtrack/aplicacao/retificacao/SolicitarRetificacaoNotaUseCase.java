package br.com.acadtrack.aplicacao.retificacao;

import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class SolicitarRetificacaoNotaUseCase {

    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;

    public SolicitarRetificacaoNotaUseCase(SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository) {
        this.solicitacaoRetificacaoRepository = solicitacaoRetificacaoRepository;
    }

    public void executar(Long id, Long notaId, String justificativa) {
        SolicitacaoRetificacao solicitacao =
                new SolicitacaoRetificacao(id, notaId, justificativa);

        solicitacaoRetificacaoRepository.salvar(solicitacao);
    }
}