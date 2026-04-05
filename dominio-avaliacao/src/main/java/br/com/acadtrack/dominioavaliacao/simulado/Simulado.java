package br.com.acadtrack.dominioavaliacao.simulado;

public class Simulado {

    private Long id;
    private String descricao;

    public Simulado(Long id, String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição do simulado não pode ser vazia");
        }

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