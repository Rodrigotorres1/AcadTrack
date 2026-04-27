package br.com.acadtrack.apresentacao.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarDisciplinaRequest(
        @NotBlank(message = "Nome da disciplina é obrigatório")
        String nome
) {
}