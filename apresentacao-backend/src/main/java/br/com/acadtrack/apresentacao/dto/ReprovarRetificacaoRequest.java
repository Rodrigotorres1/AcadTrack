package br.com.acadtrack.apresentacao.dto;

import jakarta.validation.constraints.NotBlank;

public class ReprovarRetificacaoRequest {

    @NotBlank(message = "Justificativa da decisão é obrigatória")
    private String justificativaDecisao;

    public String getJustificativaDecisao() {
        return justificativaDecisao;
    }

    public void setJustificativaDecisao(String justificativaDecisao) {
        this.justificativaDecisao = justificativaDecisao;
    }
}
