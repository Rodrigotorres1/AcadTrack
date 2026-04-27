package br.com.acadtrack.aplicacao.retificacao;

import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import br.com.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class SolicitarRetificacaoNotaUseCase {

    private final NotaRepository notaRepository;
    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;

    public SolicitarRetificacaoNotaUseCase(
            NotaRepository notaRepository,
            SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository
    ) {
        this.notaRepository = notaRepository;
        this.solicitacaoRetificacaoRepository = solicitacaoRetificacaoRepository;
    }

    public SolicitacaoRetificacao executar(Long notaId, String justificativa) {
        if (notaId == null) {
            throw new IllegalArgumentException("Nota é obrigatória");
        }

        notaRepository.buscarPorId(notaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Nota não encontrada"));

        if (justificativa == null || justificativa.isBlank()) {
            throw new IllegalArgumentException("Justificativa é obrigatória");
        }

        if (solicitacaoRetificacaoRepository.existeEmAbertoPorNotaId(notaId)) {
            throw new IllegalStateException("Já existe solicitação de retificação em aberto para esta nota");
        }

        SolicitacaoRetificacao solicitacao = new SolicitacaoRetificacao(
                null,
                notaId,
                justificativa,
                null,
                SolicitacaoRetificacao.STATUS_PENDENTE
        );

        return solicitacaoRetificacaoRepository.salvar(solicitacao);
    }
}