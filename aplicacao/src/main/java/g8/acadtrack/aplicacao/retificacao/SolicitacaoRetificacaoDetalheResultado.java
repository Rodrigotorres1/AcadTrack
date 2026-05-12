package g8.acadtrack.aplicacao.retificacao;

import g8.acadtrack.dominioavaliacao.retificacao.StatusSolicitacaoRetificacao;

public record SolicitacaoRetificacaoDetalheResultado(
        Long id,
        Long notaId,
        Long alunoId,
        String alunoNome,
        Long disciplinaId,
        String disciplinaNome,
        Long simuladoId,
        String simuladoDescricao,
        Double notaAtual,
        String justificativa,
        String justificativaDecisao,
        StatusSolicitacaoRetificacao status
) {
}
