package g8.acadtrack.dominioavaliacao.retificacao;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

public enum StatusSolicitacaoRetificacao {
    PENDENTE,
    EM_ANALISE,
    APROVADA,
    REPROVADA;

    public static StatusSolicitacaoRetificacao normalizar(String status) {
        if (status == null || status.isBlank()) {
            return PENDENTE;
        }

        String statusNormalizado = status.trim().toUpperCase();
        for (StatusSolicitacaoRetificacao statusSolicitacao : values()) {
            if (statusSolicitacao.name().equals(statusNormalizado)) {
                return statusSolicitacao;
            }
        }

        throw new RegraDeNegocioException("Status de solicitação de retificação inválido");
    }

    public static StatusSolicitacaoRetificacao normalizar(StatusSolicitacaoRetificacao status) {
        return status == null ? PENDENTE : status;
    }
}
