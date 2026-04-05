package br.com.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "simulado")
public class SimuladoJpaEntity {

    @Id
    private Long id;
    private String descricao;

    public SimuladoJpaEntity() {
    }

    public SimuladoJpaEntity(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
}