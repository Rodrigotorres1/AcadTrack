package br.com.acadtrack.aplicacao.dto;

public class CriarSimuladoCommand {

    private Long id;
    private String descricao;

    public CriarSimuladoCommand(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
}