package br.com.acadtrack.dominioavaliacao.nota;

public class Nota {

    private Long alunoId;
    private Long simuladoId;
    private Long disciplinaId;
    private double valor;

    public Nota(Long alunoId, Long simuladoId, Long disciplinaId, double valor) {

        if (valor < 0 || valor > 10) {
            throw new IllegalArgumentException("Nota deve estar entre 0 e 10");
        }

        this.alunoId = alunoId;
        this.simuladoId = simuladoId;
        this.disciplinaId = disciplinaId;
        this.valor = valor;
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
}