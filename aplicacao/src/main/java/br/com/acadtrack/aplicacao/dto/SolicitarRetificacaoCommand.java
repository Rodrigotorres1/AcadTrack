package br.com.acadtrack.aplicacao.dto;

public class SolicitarRetificacaoCommand {

    private Long id;
    private Long notaId;
    private String justificativa;

    public SolicitarRetificacaoCommand(Long id, Long notaId, String justificativa) {
        this.id = id;
        this.notaId = notaId;
        this.justificativa = justificativa;
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