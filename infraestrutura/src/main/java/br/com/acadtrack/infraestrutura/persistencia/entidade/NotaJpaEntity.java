package br.com.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "nota")
public class NotaJpaEntity {

    @Id
    private Long id;
    private Long alunoId;
    private Long simuladoId;
    private String disciplina;
    private double valor;

    public NotaJpaEntity() {
    }

    public NotaJpaEntity(Long id, Long alunoId, Long simuladoId, String disciplina, double valor) {
        this.id = id;
        this.alunoId = alunoId;
        this.simuladoId = simuladoId;
        this.disciplina = disciplina;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public Long getSimuladoId() {
        return simuladoId;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public double getValor() {
        return valor;
    }
}