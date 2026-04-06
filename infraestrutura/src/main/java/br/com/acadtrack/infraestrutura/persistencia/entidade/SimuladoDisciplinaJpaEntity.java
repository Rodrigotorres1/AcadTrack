package br.com.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.*;

@Entity
@Table(name = "simulado_disciplina")
public class SimuladoDisciplinaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long simuladoId;
    private Long disciplinaId;
    private double peso;

    public SimuladoDisciplinaJpaEntity() {
    }

    public SimuladoDisciplinaJpaEntity(Long id, Long simuladoId, Long disciplinaId, double peso) {
        this.id = id;
        this.simuladoId = simuladoId;
        this.disciplinaId = disciplinaId;
        this.peso = peso;
    }

    public Long getId() {
        return id;
    }

    public Long getSimuladoId() {
        return simuladoId;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public double getPeso() {
        return peso;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSimuladoId(Long simuladoId) {
        this.simuladoId = simuladoId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}