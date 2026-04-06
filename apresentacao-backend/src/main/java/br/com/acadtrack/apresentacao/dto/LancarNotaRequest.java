package br.com.acadtrack.apresentacao.dto;

public class LancarNotaRequest {

    private Long alunoId;
    private Long simuladoId;
    private Long disciplinaId;
    private double valor;

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