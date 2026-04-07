package br.com.acadtrack.aplicacao.retificacao;

import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("Nota não encontrada"));

        SolicitacaoRetificacao solicitacao = new SolicitacaoRetificacao(
                null,
                notaId,
                justificativa,
                "PENDENTE"
        );

        return solicitacaoRetificacaoRepository.salvar(solicitacao);
    }
}