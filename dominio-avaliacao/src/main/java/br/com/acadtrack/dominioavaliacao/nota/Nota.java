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

        if (valor < 0) {
            throw new IllegalArgumentException("Valor da nota não pode ser negativo");
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
}