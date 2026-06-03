package g8.acadtrack.dominioavaliacao.retificacao;

// PADRÃO: Factory (GoF) — centraliza a criação de novas solicitações de retificação,
// garantindo que o estado inicial PENDENTE e a ausência de justificativa de decisão
// sejam invariantes do domínio, não responsabilidade do chamador.
public final class SolicitacaoRetificacaoFabrica {

    private SolicitacaoRetificacaoFabrica() {
    }

    public static SolicitacaoRetificacao nova(Long notaId, String justificativa) {
        return new SolicitacaoRetificacao(
                null,
                notaId,
                justificativa,
                null,
                StatusSolicitacaoRetificacao.PENDENTE
        );
    }
}
