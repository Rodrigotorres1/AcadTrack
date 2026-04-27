package br.com.acadtrack.apresentacao.dto;

import jakarta.validation.constraints.NotNull;

public class VincularAlunoTurmaRequest {

    @NotNull(message = "Turma é obrigatória")
    private Long turmaId;

    public VincularAlunoTurmaRequest() {
    }

    public Long getTurmaId() {
        return turmaId;
    }
}