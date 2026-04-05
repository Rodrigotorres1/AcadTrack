package br.com.acadtrack.apresentacao.dto;

public class SolicitarRetificacaoRequest {

    private Long id;
    private Long notaId;
    private String justificativa;

    public SolicitarRetificacaoRequest() {
    }

    public Long getId() {
        return id;
    }

    public Long getNotaId() {
        return notaId;
    }

    public String getJustificativa() {
        return justificativa;
    }
}