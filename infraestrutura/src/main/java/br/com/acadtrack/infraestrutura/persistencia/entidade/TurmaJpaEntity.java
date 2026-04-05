package br.com.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "turma")
public class TurmaJpaEntity {

    @Id
    private Long id;
    private String nome;

    public TurmaJpaEntity() {
    }

    public TurmaJpaEntity(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}