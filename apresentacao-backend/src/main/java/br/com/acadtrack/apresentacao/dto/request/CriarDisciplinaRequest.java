package br.com.acadtrack.apresentacao.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CriarDisciplinaRequest(
        @NotBlank(message = "Nome da disciplina é obrigatório")
        String nome
) {
}