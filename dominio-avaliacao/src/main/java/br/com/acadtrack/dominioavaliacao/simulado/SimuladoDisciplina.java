package br.com.acadtrack.dominioavaliacao.simulado;

public class SimuladoDisciplina {

    private Long id;
    private Long simuladoId;
    private Long disciplinaId;
    private double peso;

    public SimuladoDisciplina(Long id, Long simuladoId, Long disciplinaId, double peso) {

        if (peso <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }

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
}