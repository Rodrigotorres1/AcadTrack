package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.aplicacao.retificacao.SolicitacaoRetificacaoDetalheResultado;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;

public class SolicitacaoRetificacaoResponse {

    private Long id;
    private Long notaId;
    private Long alunoId;
    private String alunoNome;
    private Long disciplinaId;
    private String disciplinaNome;
    private Long simuladoId;
    private String simuladoDescricao;
    private Double notaAtual;
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
        this(id, notaId, null, null, null, null, null, null, null, justificativa, justificativaDecisao, status);
    }

    public SolicitacaoRetificacaoResponse(
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
            String status
    ) {
        this.id = id;
        this.notaId = notaId;
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.disciplinaId = disciplinaId;
        this.disciplinaNome = disciplinaNome;
        this.simuladoId = simuladoId;
        this.simuladoDescricao = simuladoDescricao;
        this.notaAtual = notaAtual;
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
                solicitacao.getStatus().name()
        );
    }

    public static SolicitacaoRetificacaoResponse fromApplication(SolicitacaoRetificacaoDetalheResultado solicitacao) {
        return new SolicitacaoRetificacaoResponse(
                solicitacao.id(),
                solicitacao.notaId(),
                solicitacao.alunoId(),
                solicitacao.alunoNome(),
                solicitacao.disciplinaId(),
                solicitacao.disciplinaNome(),
                solicitacao.simuladoId(),
                solicitacao.simuladoDescricao(),
                solicitacao.notaAtual(),
                solicitacao.justificativa(),
                solicitacao.justificativaDecisao(),
                solicitacao.status().name()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getNotaId() {
        return notaId;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public String getDisciplinaNome() {
        return disciplinaNome;
    }

    public Long getSimuladoId() {
        return simuladoId;
    }

    public String getSimuladoDescricao() {
        return simuladoDescricao;
    }

    public Double getNotaAtual() {
        return notaAtual;
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
