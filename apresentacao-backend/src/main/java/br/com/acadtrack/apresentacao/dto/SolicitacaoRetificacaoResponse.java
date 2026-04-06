package br.com.acadtrack.apresentacao.dto;

import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;

public class SolicitacaoRetificacaoResponse {

    private Long id;
    private Long notaId;
    private String justificativa;
    private String status;

    public SolicitacaoRetificacaoResponse(Long id, Long notaId, String justificativa, String status) {
        this.id = id;
        this.notaId = notaId;
        this.justificativa = justificativa;
        this.status = status;
    }

    public static SolicitacaoRetificacaoResponse fromDomain(SolicitacaoRetificacao solicitacao) {
        return new SolicitacaoRetificacaoResponse(
                solicitacao.getId(),
                solicitacao.getNotaId(),
                solicitacao.getJustificativa(),
                solicitacao.getStatus()
        );
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
}