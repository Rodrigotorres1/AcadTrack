package g8.acadtrack.infraestrutura.persistencia.entidade;

import g8.acadtrack.dominioavaliacao.retificacao.StatusSolicitacaoRetificacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitacao_retificacao")
public class SolicitacaoRetificacaoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nota_id")
    private Long notaId;
    private String justificativa;
    private String justificativaDecisao;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacaoRetificacao status;

    public SolicitacaoRetificacaoJpaEntity() {
    }

    public SolicitacaoRetificacaoJpaEntity(
            Long id,
            Long notaId,
            String justificativa,
            String justificativaDecisao,
            StatusSolicitacaoRetificacao status
    ) {
        this.id = id;
        this.notaId = notaId;
        this.justificativa = justificativa;
        this.justificativaDecisao = justificativaDecisao;
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

    public StatusSolicitacaoRetificacao getStatus() {
        return status;
    }

    public String getJustificativaDecisao() {
        return justificativaDecisao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNotaId(Long notaId) {
        this.notaId = notaId;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public void setJustificativaDecisao(String justificativaDecisao) {
        this.justificativaDecisao = justificativaDecisao;
    }

    public void setStatus(StatusSolicitacaoRetificacao status) {
        this.status = status;
    }
}
