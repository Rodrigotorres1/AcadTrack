package g8.acadtrack.dominiousuarios.notificacao;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;

import java.time.LocalDateTime;

public class NotificacaoResponsavel {

    private Long id;
    private Long alunoId;
    private Long responsavelId;
    private NivelRiscoAcademico nivelRisco;
    private PrioridadeNotificacao prioridade;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private StatusNotificacao status;

    public NotificacaoResponsavel(
            Long id,
            Long alunoId,
            Long responsavelId,
            NivelRiscoAcademico nivelRisco,
            PrioridadeNotificacao prioridade,
            String mensagem,
            LocalDateTime dataCriacao,
            StatusNotificacao status
    ) {
        if (alunoId == null) {
            throw new RegraDeNegocioException("Aluno é obrigatório para notificação");
        }
        if (responsavelId == null) {
            throw new RegraDeNegocioException("Responsável é obrigatório para notificação");
        }
        if (nivelRisco == null) {
            throw new RegraDeNegocioException("Nível de risco é obrigatório para notificação");
        }
        if (prioridade == null) {
            throw new RegraDeNegocioException("Prioridade é obrigatória para notificação");
        }
        if (mensagem == null || mensagem.isBlank()) {
            throw new RegraDeNegocioException("Mensagem é obrigatória para notificação");
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

    public NivelRiscoAcademico getNivelRisco() {
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
