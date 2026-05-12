package g8.acadtrack.dominioavaliacao.retificacao;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

import java.util.Objects;

public class SolicitacaoRetificacao {

    public static final StatusSolicitacaoRetificacao STATUS_PENDENTE = StatusSolicitacaoRetificacao.PENDENTE;
    public static final StatusSolicitacaoRetificacao STATUS_EM_ANALISE = StatusSolicitacaoRetificacao.EM_ANALISE;
    public static final StatusSolicitacaoRetificacao STATUS_APROVADA = StatusSolicitacaoRetificacao.APROVADA;
    public static final StatusSolicitacaoRetificacao STATUS_REPROVADA = StatusSolicitacaoRetificacao.REPROVADA;

    private Long id;
    private Long notaId;
    private String justificativa;
    private String justificativaDecisao;
    private StatusSolicitacaoRetificacao status;

    public SolicitacaoRetificacao(
            Long id,
            Long notaId,
            String justificativa,
            String justificativaDecisao,
            String status
    ) {
        this(id, notaId, justificativa, justificativaDecisao, StatusSolicitacaoRetificacao.normalizar(status));
    }

    public SolicitacaoRetificacao(
            Long id,
            Long notaId,
            String justificativa,
            String justificativaDecisao,
            StatusSolicitacaoRetificacao status
    ) {
        if (notaId == null) {
            throw new RegraDeNegocioException("Nota é obrigatória");
        }

        this.id = id;
        this.notaId = notaId;
        this.justificativa = justificativa;
        this.justificativaDecisao = normalizarJustificativaDecisao(justificativaDecisao);
        this.status = StatusSolicitacaoRetificacao.normalizar(status);
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

    public StatusSolicitacaoRetificacao getStatus() {
        return status;
    }

    public String getJustificativaDecisao() {
        return justificativaDecisao;
    }

    public void iniciarAnalise() {
        if (!Objects.equals(status, StatusSolicitacaoRetificacao.PENDENTE)) {
            throw new IllegalStateException("A solicitação deve estar pendente para iniciar análise");
        }
        this.status = StatusSolicitacaoRetificacao.EM_ANALISE;
    }

    public void aprovar(String justificativaDecisao) {
        if (!Objects.equals(status, StatusSolicitacaoRetificacao.EM_ANALISE)) {
            throw new IllegalStateException("A solicitação deve estar em análise para aprovação");
        }
        if (justificativaDecisao == null || justificativaDecisao.isBlank()) {
            throw new RegraDeNegocioException("Justificativa é obrigatória");
        }
        this.justificativaDecisao = justificativaDecisao.trim();
        this.status = StatusSolicitacaoRetificacao.APROVADA;
    }

    public void reprovar(String justificativaDecisao) {
        if (!Objects.equals(status, StatusSolicitacaoRetificacao.EM_ANALISE)) {
            throw new IllegalStateException("A solicitação deve estar em análise para reprovação");
        }
        if (justificativaDecisao == null || justificativaDecisao.isBlank()) {
            throw new RegraDeNegocioException("Justificativa é obrigatória");
        }
        this.justificativaDecisao = justificativaDecisao.trim();
        this.status = StatusSolicitacaoRetificacao.REPROVADA;
    }

    public boolean estaEmAberto() {
        return Objects.equals(status, StatusSolicitacaoRetificacao.PENDENTE)
                || Objects.equals(status, StatusSolicitacaoRetificacao.EM_ANALISE);
    }

    private String normalizarJustificativaDecisao(String justificativaDecisao) {
        if (justificativaDecisao == null || justificativaDecisao.isBlank()) {
            return null;
        }
        return justificativaDecisao.trim();
    }
}
