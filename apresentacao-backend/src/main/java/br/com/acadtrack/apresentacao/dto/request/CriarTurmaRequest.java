package br.com.acadtrack.apresentacao.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CriarTurmaRequest {

    @NotBlank(message = "Nome da turma é obrigatório")
    private String nome;

    public CriarTurmaRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}