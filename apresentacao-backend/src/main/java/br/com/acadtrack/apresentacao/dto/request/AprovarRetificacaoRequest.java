package br.com.acadtrack.apresentacao.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public class AprovarRetificacaoRequest {

    @DecimalMin(value = "0.0", message = "A nota deve estar entre 0 e 10")
    @DecimalMax(value = "10.0", message = "A nota deve estar entre 0 e 10")
    private double novoValorNota;

    @NotBlank(message = "Justificativa da decisão é obrigatória")
    private String justificativaDecisao;

    public double getNovoValorNota() {
        return novoValorNota;
    }

    public void setNovoValorNota(double novoValorNota) {
        this.novoValorNota = novoValorNota;
    }

    public String getJustificativaDecisao() {
        return justificativaDecisao;
    }

    public void setJustificativaDecisao(String justificativaDecisao) {
        this.justificativaDecisao = justificativaDecisao;
    }
}
