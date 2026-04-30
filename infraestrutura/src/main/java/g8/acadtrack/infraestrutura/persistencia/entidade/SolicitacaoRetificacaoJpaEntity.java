package g8.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Entity;
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

    private Long notaId;
    private String justificativa;
    private String justificativaDecisao;
    private String status;

    public SolicitacaoRetificacaoJpaEntity() {
    }

    public SolicitacaoRetificacaoJpaEntity(
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

    public void setStatus(String status) {
        this.status = status;
    }
}