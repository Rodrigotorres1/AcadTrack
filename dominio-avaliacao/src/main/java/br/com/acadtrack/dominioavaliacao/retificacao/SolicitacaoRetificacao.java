package br.com.acadtrack.dominioavaliacao.retificacao;

public class SolicitacaoRetificacao {

    private Long id;
    private Long notaId;
    private String justificativa;
    private String status;

    public SolicitacaoRetificacao(Long id, Long notaId, String justificativa, String status) {
        if (notaId == null) {
            throw new IllegalArgumentException("Nota é obrigatória");
        }

        if (justificativa == null || justificativa.isBlank()) {
            throw new IllegalArgumentException("Justificativa é obrigatória");
        }

        this.id = id;
        this.notaId = notaId;
        this.justificativa = justificativa;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getNotaId() {
        return notaId;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public String getStatus() {
        return status;
    }

    public void aprovar() {
        if (!status.equals("PENDENTE")) {
            throw new IllegalStateException("A solicitação não pode mais ser aprovada");
        }
        this.status = "APROVADA";
    }

    public void rejeitar() {
        if (!status.equals("PENDENTE")) {
            throw new IllegalStateException("A solicitação não pode mais ser rejeitada");
        }
        this.status = "REJEITADA";
    }
}