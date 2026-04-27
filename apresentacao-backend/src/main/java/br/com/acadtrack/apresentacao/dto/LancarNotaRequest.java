package br.com.acadtrack.apresentacao.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class LancarNotaRequest {

    @NotNull(message = "Aluno é obrigatório")
    private Long alunoId;

    @NotNull(message = "Simulado é obrigatório")
    private Long simuladoId;

    @NotNull(message = "Disciplina é obrigatória")
    private Long disciplinaId;

    @DecimalMin(value = "0.0", message = "A nota deve estar entre 0 e 10")
    @DecimalMax(value = "10.0", message = "A nota deve estar entre 0 e 10")
    private double valor;

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