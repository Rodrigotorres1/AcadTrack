package br.com.acadtrack.aplicacao.dto;

public class LancarNotaCommand {

    private Long id;
    private Long alunoId;
    private Long simuladoId;
    private String disciplina;
    private double valor;

    public LancarNotaCommand(Long id, Long alunoId, Long simuladoId, String disciplina, double valor) {
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