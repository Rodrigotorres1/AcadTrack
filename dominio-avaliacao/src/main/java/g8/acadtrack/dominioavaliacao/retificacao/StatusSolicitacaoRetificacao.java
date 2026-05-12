package g8.acadtrack.dominioavaliacao.retificacao;

public enum StatusSolicitacaoRetificacao {
    PENDENTE,
    EM_ANALISE,
    APROVADA,
    REPROVADA;

    public static StatusSolicitacaoRetificacao normalizar(String status) {
        if (status == null || status.isBlank()) {
            return PENDENTE;
        }
        try {
            return StatusSolicitacaoRetificacao.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de solicitação de retificação inválido", e);
        }
    }

    public static StatusSolicitacaoRetificacao normalizar(StatusSolicitacaoRetificacao status) {
        return status == null ? PENDENTE : status;
    }
}
