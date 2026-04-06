package br.com.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "nota")
public class NotaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long alunoId;
    private Long simuladoId;
    private Long disciplinaId;
    private double valor;

    public NotaJpaEntity() {
    }

    public NotaJpaEntity(Long id, Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        this.id = id;
        this.alunoId = alunoId;
        this.simuladoId = simuladoId;
        this.disciplinaId = disciplinaId;
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

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public double getValor() {
        return valor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public void setSimuladoId(Long simuladoId) {
        this.simuladoId = simuladoId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}