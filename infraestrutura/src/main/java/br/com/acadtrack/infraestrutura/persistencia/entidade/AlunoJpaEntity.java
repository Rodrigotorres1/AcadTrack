package br.com.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aluno")
public class AlunoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private Long turmaId;

    public AlunoJpaEntity() {
    }

    public AlunoJpaEntity(Long id, String nome, String email, Long turmaId) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.turmaId = turmaId;
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

    public Long getTurmaId() {
        return turmaId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }
}