package g8.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private String nivelRisco;
    private String prioridade;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private String status;

    public NotificacaoResponsavelJpaEntity() {
    }

    public NotificacaoResponsavelJpaEntity(
            Long id,
            Long alunoId,
            Long responsavelId,
            String nivelRisco,
            String prioridade,
            String mensagem,
            LocalDateTime dataCriacao,
            String status
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

    public String getNivelRisco() {
        return nivelRisco;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public String getStatus() {
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

    public void setNivelRisco(String nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
