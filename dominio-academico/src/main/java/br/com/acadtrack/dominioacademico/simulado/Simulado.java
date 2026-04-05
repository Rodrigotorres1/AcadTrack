package br.com.acadtrack.dominioacademico.simulado;

public class Simulado {

    private Long id;
    private String descricao;

    public Simulado(Long id, String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição inválida");
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