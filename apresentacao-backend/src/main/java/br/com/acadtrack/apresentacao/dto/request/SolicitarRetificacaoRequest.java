package br.com.acadtrack.apresentacao.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitarRetificacaoRequest {

    @NotNull(message = "Nota é obrigatória")
    private Long notaId;

    @NotBlank(message = "Justificativa é obrigatória")
    private String justificativa;

    public SolicitarRetificacaoRequest() {
    }

    public Long getNotaId() {
        return notaId;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setNotaId(Long notaId) {
        this.notaId = notaId;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}