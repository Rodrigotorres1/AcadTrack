package br.com.acadtrack.dominioavaliacao.nota;

public class Nota {

    private Long id;
    private Long alunoId;
    private Long simuladoId;
    private String disciplina;
    private double valor;

    public Nota(Long id, Long alunoId, Long simuladoId, String disciplina, double valor) {
        if (alunoId == null) {
            throw new IllegalArgumentException("Aluno é obrigatório");
        }

        if (simuladoId == null) {
            throw new IllegalArgumentException("Simulado é obrigatório");
        }

        if (disciplina == null || disciplina.isBlank()) {
            throw new IllegalArgumentException("Disciplina é obrigatória");
        }

        if (valor < 0 || valor > 10) {
            throw new IllegalArgumentException("Nota inválida");
        }

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

    public void alterarValor(double novoValor) {
        if (novoValor < 0 || novoValor > 10) {
            throw new IllegalArgumentException("Nota inválida");
        }
        this.valor = novoValor;
    }
}