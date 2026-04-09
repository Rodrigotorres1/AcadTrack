package br.com.acadtrack.dominioavaliacao.nota;

public class Nota {

    private Long id;
    private Long alunoId;
    private Long simuladoId;
    private Long disciplinaId;
    private double valor;

    public Nota(Long id, Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        if (alunoId == null) {
            throw new IllegalArgumentException("Aluno é obrigatório");
        }
        if (simuladoId == null) {
            throw new IllegalArgumentException("Simulado é obrigatório");
        }
        if (disciplinaId == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória");
        }
        if (valor < 0 || valor > 10) {
            throw new IllegalArgumentException("Nota deve estar entre 0 e 10");
        }

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
}