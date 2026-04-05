package br.com.acadtrack.apresentacao.dto;

public class LancarNotaRequest {

    private Long alunoId;
    private Long simuladoId;
    private String disciplina;
    private double valor;

    public LancarNotaRequest() {
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