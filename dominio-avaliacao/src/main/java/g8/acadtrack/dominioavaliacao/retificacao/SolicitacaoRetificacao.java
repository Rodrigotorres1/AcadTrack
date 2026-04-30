package g8.acadtrack.dominioavaliacao.retificacao;

import java.util.Objects;

public class SolicitacaoRetificacao {

    public static final String STATUS_PENDENTE = "PENDENTE";
    public static final String STATUS_EM_ANALISE = "EM_ANALISE";
    public static final String STATUS_APROVADA = "APROVADA";
    public static final String STATUS_REPROVADA = "REPROVADA";

    private Long id;
    private Long notaId;
    private String justificativa;
    private String justificativaDecisao;
    private String status;

    public SolicitacaoRetificacao(
            Long id,
            Long notaId,
            String justificativa,
            String justificativaDecisao,
            String status
    ) {
        if (notaId == null) {
            throw new IllegalArgumentException("Nota é obrigatória");
        }

        if (justificativa == null || justificativa.isBlank()) {
            throw new IllegalArgumentException("Justificativa é obrigatória");
        }

        this.id = id;
        this.notaId = notaId;
        this.justificativa = justificativa;
        this.justificativaDecisao = normalizarJustificativaDecisao(justificativaDecisao);
        this.status = normalizarStatus(status);
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

    public void iniciarAnalise() {
        if (!Objects.equals(status, STATUS_PENDENTE)) {
            throw new IllegalStateException("A solicitação deve estar pendente para iniciar análise");
        }
        this.status = STATUS_EM_ANALISE;
    }

    public void aprovar(String justificativaDecisao) {
        if (!Objects.equals(status, STATUS_EM_ANALISE)) {
            throw new IllegalStateException("A solicitação deve estar em análise para aprovação");
        }
        if (justificativaDecisao == null || justificativaDecisao.isBlank()) {
            throw new IllegalArgumentException("Justificativa da decisão é obrigatória");
        }
        this.justificativaDecisao = justificativaDecisao.trim();
        this.status = STATUS_APROVADA;
    }

    public void reprovar(String justificativaDecisao) {
        if (!Objects.equals(status, STATUS_EM_ANALISE)) {
            throw new IllegalStateException("A solicitação deve estar em análise para reprovação");
        }
        if (justificativaDecisao == null || justificativaDecisao.isBlank()) {
            throw new IllegalArgumentException("Justificativa da decisão é obrigatória");
        }
        this.justificativaDecisao = justificativaDecisao.trim();
        this.status = STATUS_REPROVADA;
    }

    public boolean estaEmAberto() {
        return Objects.equals(status, STATUS_PENDENTE) || Objects.equals(status, STATUS_EM_ANALISE);
    }

    private String normalizarStatus(String status) {
        if (status == null || status.isBlank()) {
            return STATUS_PENDENTE;
        }

        String statusNormalizado = status.trim().toUpperCase();
        if (Objects.equals(statusNormalizado, "REJEITADA")) {
            return STATUS_REPROVADA;
        }

        if (
                !Objects.equals(statusNormalizado, STATUS_PENDENTE)
                        && !Objects.equals(statusNormalizado, STATUS_EM_ANALISE)
                        && !Objects.equals(statusNormalizado, STATUS_APROVADA)
                        && !Objects.equals(statusNormalizado, STATUS_REPROVADA)
        ) {
            throw new IllegalArgumentException("Status de solicitação de retificação inválido");
        }
        return statusNormalizado;
    }

    private String normalizarJustificativaDecisao(String justificativaDecisao) {
        if (justificativaDecisao == null || justificativaDecisao.isBlank()) {
            return null;
        }
        return justificativaDecisao.trim();
    }
}
