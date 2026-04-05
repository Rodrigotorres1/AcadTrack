package br.com.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitacao_retificacao")
public class SolicitacaoRetificacaoJpaEntity {

    @Id
    private Long id;
    private Long notaId;
    private String justificativa;
    private String status;

    public SolicitacaoRetificacaoJpaEntity() {
    }

    public SolicitacaoRetificacaoJpaEntity(Long id, Long notaId, String justificativa, String status) {
        this.id = id;
        this.notaId = notaId;
        this.justificativa = justificativa;
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
}