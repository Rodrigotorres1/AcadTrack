package g8.acadtrack.infraestrutura.persistencia.entidade;

import g8.acadtrack.dominiousuarios.notificacao.PrioridadeNotificacao;
import g8.acadtrack.dominiousuarios.notificacao.StatusNotificacao;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao_responsavel")
public class NotificacaoResponsavelJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aluno_id")
    private Long alunoId;

    @Column(name = "responsavel_id")
    private Long responsavelId;

    @Enumerated(EnumType.STRING)
    private NivelRiscoAcademico nivelRisco;

    @Enumerated(EnumType.STRING)
    private PrioridadeNotificacao prioridade;

    private String mensagem;
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private StatusNotificacao status;

    public NotificacaoResponsavelJpaEntity() {
    }

    public NotificacaoResponsavelJpaEntity(
            Long id,
            Long alunoId,
            Long responsavelId,
            NivelRiscoAcademico nivelRisco,
            PrioridadeNotificacao prioridade,
            String mensagem,
            LocalDateTime dataCriacao,
            StatusNotificacao status
    ) {
        this.id = id;
        this.alunoId = alunoId;
        this.responsavelId = responsavelId;
        this.nivelRisco = nivelRisco;
        this.prioridade = prioridade;
        this.mensagem = mensagem;
        this.dataCriacao = dataCriacao;
        this.status = status;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }

    public void setNivelRisco(NivelRiscoAcademico nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public void setPrioridade(PrioridadeNotificacao prioridade) {
        this.prioridade = prioridade;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setStatus(StatusNotificacao status) {
        this.status = status;
    }
}
