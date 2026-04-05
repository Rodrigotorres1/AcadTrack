package br.com.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "responsavel")
public class ResponsavelJpaEntity {

    @Id
    private Long id;
    private String nome;
    private String email;
    private Long alunoId;

    public ResponsavelJpaEntity() {
    }

    public ResponsavelJpaEntity(Long id, String nome, String email, Long alunoId) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.alunoId = alunoId;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }
}