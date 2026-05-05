package g8.acadtrack.dominiousuarios.notificacao;

import java.time.LocalDateTime;

public class NotificacaoResponsavel {

    private Long id;
    private Long alunoId;
    private Long responsavelId;
    private String nivelRisco;
    private PrioridadeNotificacao prioridade;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private StatusNotificacao status;

    public NotificacaoResponsavel(
            Long id,
            Long alunoId,
            Long responsavelId,
            String nivelRisco,
            PrioridadeNotificacao prioridade,
            String mensagem,
            LocalDateTime dataCriacao,
            StatusNotificacao status
    ) {
        if (alunoId == null) {
            throw new IllegalArgumentException("Aluno e obrigatorio para notificacao");
        }
        if (responsavelId == null) {
            throw new IllegalArgumentException("Responsavel e obrigatorio para notificacao");
        }
        if (nivelRisco == null || nivelRisco.isBlank()) {
            throw new IllegalArgumentException("Nivel de risco e obrigatorio para notificacao");
        }
        if (prioridade == null) {
            throw new IllegalArgumentException("Prioridade e obrigatoria para notificacao");
        }
        if (mensagem == null || mensagem.isBlank()) {
            throw new IllegalArgumentException("Mensagem e obrigatoria para notificacao");
        }

        this.id = id;
        this.alunoId = alunoId;
        this.responsavelId = responsavelId;
        this.nivelRisco = nivelRisco;
        this.prioridade = prioridade;
        this.mensagem = mensagem;
        this.dataCriacao = dataCriacao == null ? LocalDateTime.now() : dataCriacao;
        this.status = status == null ? StatusNotificacao.NAO_LIDA : status;
    }

    public Long getId() {
        return id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public Long getResponsavelId() {
        return responsavelId;
    }

    public String getNivelRisco() {
        return nivelRisco;
    }

    public PrioridadeNotificacao getPrioridade() {
        return prioridade;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public StatusNotificacao getStatus() {
        return status;
    }
}
