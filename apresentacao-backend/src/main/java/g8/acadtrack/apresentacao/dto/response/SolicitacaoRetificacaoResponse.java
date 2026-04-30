package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;

public class SolicitacaoRetificacaoResponse {

    private Long id;
    private Long notaId;
    private String justificativa;
    private String justificativaDecisao;
    private String status;

    public SolicitacaoRetificacaoResponse(
            Long id,
            Long notaId,
            String justificativa,
            String justificativaDecisao,
            String status
    ) {
        this.id = id;
        this.notaId = notaId;
        this.justificativa = justificativa;
        this.justificativaDecisao = justificativaDecisao;
        this.status = status;
    }

    public static SolicitacaoRetificacaoResponse fromDomain(SolicitacaoRetificacao solicitacao) {
        return new SolicitacaoRetificacaoResponse(
                solicitacao.getId(),
                solicitacao.getNotaId(),
                solicitacao.getJustificativa(),
                solicitacao.getJustificativaDecisao(),
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

    public String getJustificativaDecisao() {
        return justificativaDecisao;
    }
}