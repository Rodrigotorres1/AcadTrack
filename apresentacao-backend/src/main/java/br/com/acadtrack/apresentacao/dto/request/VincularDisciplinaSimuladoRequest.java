package br.com.acadtrack.apresentacao.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class VincularDisciplinaSimuladoRequest {

    @NotNull(message = "Disciplina é obrigatória")
    private Long disciplinaId;

    @DecimalMin(value = "0.01", message = "Peso deve ser maior que zero")
    private double peso;

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public double getPeso() {
        return peso;
    }
}